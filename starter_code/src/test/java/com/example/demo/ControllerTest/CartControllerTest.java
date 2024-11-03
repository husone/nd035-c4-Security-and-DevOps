package com.example.demo.ControllerTest;

import com.example.demo.TestUtils;

import static com.example.demo.TestUtils.*;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.*;
import com.example.demo.model.persistence.repositories.*;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CartControllerTest {
    CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addTocart_UserNotFound() {
        ModifyCartRequest request = createModifyCartRequest();
        String username = "husone";
        when(userRepository.findByUsername(username)).thenReturn(null);
        ResponseEntity<Cart> response = cartController.addTocart(request);
        HttpStatus actualStatus = response.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, actualStatus);
    }

    @Test
    public void addTocart_ItemNotFound() {
        ModifyCartRequest request = createModifyCartRequest();
        String username = "husone";
        User user = new User();
        user.setUsername(username);
        user.setId(1L);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Cart> response = cartController.addTocart(request);
        HttpStatus actualStatus = response.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, actualStatus);
    }

    @Test
    public void addTocart_HappyPath() {
        ModifyCartRequest request = createModifyCartRequest();
        String username = "husone";
        User user = createUser();
        Item item = createItem(1L);
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.addTocart(request);

        Cart actualCart = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(username, actualCart.getUser().getUsername());
        assertEquals(item, actualCart.getItems().get(0));
    }

    @Test
    public void removeFromcart_HappyPath() {
        ModifyCartRequest request = createModifyCartRequest();
        String username = "husone";
        User user = createUser();
        Item item = createItem(1L);
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        Cart actualCart = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(username, actualCart.getUser().getUsername());
    }

    @Test
    public void removeFromcart_UserNotFound() {
        ModifyCartRequest request = createModifyCartRequest();
        String username = "husone";
        when(userRepository.findByUsername(username)).thenReturn(null);
        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        HttpStatus actualStatus = response.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, actualStatus);

    }

    @Test
    public void removeFromcart_ItemNotFound() {
        ModifyCartRequest request = createModifyCartRequest();
        String username = "husone";
        User user = new User();
        user.setUsername(username);
        user.setId(1L);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        HttpStatus actualStatus = response.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, actualStatus);

    }

}
