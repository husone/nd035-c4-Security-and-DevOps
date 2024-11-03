package com.example.demo.ControllerTest;

import com.example.demo.TestUtils;
import static com.example.demo.TestUtils.*;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderControllerTest {
    OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void Setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void submit_UserNotFound() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<UserOrder> response = orderController.submit("husone");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void submit_HappyPath() {
        User user = createUser();
        when(userRepository.findByUsername("husone")).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit("husone");
        UserOrder order = response.getBody();
        verify(userRepository, times(1)).findByUsername("husone");
        verify(orderRepository, times(1)).save(order);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createItem(1L), order.getItems().get(0));
        assertEquals(createUser().getId(), order.getUser().getId());
    }

    @Test
    public void getOrdersForUser_UserNotFound() {
        when(userRepository.findByUsername("husone")).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("husone");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getOrdersForUser_HappyPath() {
        User user = createUser();
        UserOrder order = UserOrder.createFromCart(user.getCart());
        List<UserOrder> orders = new ArrayList<>();
        orders.add(order);
        when(userRepository.findByUsername("husone")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(orders);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("husone");
        List<UserOrder> actualOrders = response.getBody();
        verify(userRepository, times(1)).findByUsername("husone");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createItem(1L), order.getItems().get(0));
        assertEquals(createUser().getId(), order.getUser().getId());
    }

}
