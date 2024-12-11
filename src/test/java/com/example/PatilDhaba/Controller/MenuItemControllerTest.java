package com.example.PatilDhaba.Controller;

import com.example.PatilDhaba.model.MenuItem;
import com.example.PatilDhaba.service.MenuItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuItemControllerTest {

    @Mock
    private MenuItemService menuItemService;

    @InjectMocks
    private MenuItemController menuItemController;

    private MenuItem menuItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        menuItem = new MenuItem();
        menuItem.setName("Burger");
        menuItem.setPrice(150.0);
        menuItem.setDescription("Delicious chicken burger");
    }

    @Test
    void addMenuItem_Success() {
        when(menuItemService.addMenuItem(menuItem)).thenReturn(menuItem);

        ResponseEntity<MenuItem> response = menuItemController.addMenuItem(menuItem);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(menuItem, response.getBody());
        verify(menuItemService, times(1)).addMenuItem(menuItem);
    }

    @Test
    void getAllMenuItems_Success() {
        List<MenuItem> menuItems = Arrays.asList(menuItem);
        when(menuItemService.getAllMenuItems()).thenReturn(menuItems);

        ResponseEntity<List<MenuItem>> response = menuItemController.getAllMenuItems();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(menuItems, response.getBody());
        verify(menuItemService, times(1)).getAllMenuItems();
    }

    @Test
    void getMenuItemByName_Success() {
        when(menuItemService.getMenuItemByName("Burger")).thenReturn(Optional.of(menuItem));

        ResponseEntity<MenuItem> response = menuItemController.getMenuItemByName("Burger");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(menuItem, response.getBody());
        verify(menuItemService, times(1)).getMenuItemByName("Burger");
    }

    @Test
    void getMenuItemByName_NotFound() {
        when(menuItemService.getMenuItemByName("Pizza")).thenReturn(Optional.empty());

        ResponseEntity<MenuItem> response = menuItemController.getMenuItemByName("Pizza");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(menuItemService, times(1)).getMenuItemByName("Pizza");
    }

    @Test
    void updateMenuItem_Success() {
        MenuItem updatedMenuItem = new MenuItem();
        updatedMenuItem.setName("Burger");
        updatedMenuItem.setPrice(180.0);
        updatedMenuItem.setDescription("Updated chicken burger");

        when(menuItemService.updateMenuItem("Burger", updatedMenuItem)).thenReturn(updatedMenuItem);

        ResponseEntity<MenuItem> response = menuItemController.updateMenuItem("Burger", updatedMenuItem);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedMenuItem, response.getBody());
        verify(menuItemService, times(1)).updateMenuItem("Burger", updatedMenuItem);
    }

    @Test
    void updateMenuItem_NotFound() {
        MenuItem updatedMenuItem = new MenuItem();
        updatedMenuItem.setName("Burger");
        updatedMenuItem.setPrice(180.0);
        updatedMenuItem.setDescription("Updated chicken burger");

        when(menuItemService.updateMenuItem("Pizza", updatedMenuItem)).thenReturn(null);

        ResponseEntity<MenuItem> response = menuItemController.updateMenuItem("Pizza", updatedMenuItem);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(menuItemService, times(1)).updateMenuItem("Pizza", updatedMenuItem);
    }

    @Test
    void deleteMenuItem_Success() {
        doNothing().when(menuItemService).deleteMenuItem("Burger");

        ResponseEntity<Void> response = menuItemController.deleteMenuItem("Burger");

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(menuItemService, times(1)).deleteMenuItem("Burger");
    }
}
