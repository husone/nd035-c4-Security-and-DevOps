package com.example.demo.ControllerTest;

import com.example.demo.TestUtils;
import static com.example.demo.TestUtils.createItem;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void Setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItems() {
        Item item = createItem(1L);
        List<Item> items = new ArrayList<>();
        items.add(item);
        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> actualItems = response.getBody();
        assertEquals(1, actualItems.size());
        assertEquals(item,actualItems.get(0));
    }

    @Test
    public void getItemById() {
        Item item = createItem(1L);
        Item expectItem = createItem(1L);

        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Item actualItems = response.getBody();
        assertEquals(expectItem.getPrice(), actualItems.getPrice());
        assertEquals(expectItem.getName(),actualItems.getName());
    }

    @Test
    public void getItemByName() {
        Item item = createItem(1L);
        List<Item> items = new ArrayList<>();
        items.add(item);
        when(itemRepository.findByName(any())).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("husone");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> actualItems = response.getBody();
        assertEquals(1,actualItems.size());

    }

    @Test
    public void getItemByName_NotFound() {
        Item item = createItem(1L);
        List<Item> items = new ArrayList<>();
        items.add(item);
        when(itemRepository.findByName(any())).thenReturn(null);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("husone");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

}
