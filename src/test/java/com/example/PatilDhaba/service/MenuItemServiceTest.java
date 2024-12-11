package com.example.PatilDhaba.service;

import com.example.PatilDhaba.model.MenuItem;
import com.example.PatilDhaba.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuItemService menuItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddMenuItem_Success() {
        MenuItem newMenuItem = new MenuItem();
        newMenuItem.setName("Pizza");
        newMenuItem.setPrice(10.99);

        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(newMenuItem);
        MenuItem savedMenuItem = menuItemService.addMenuItem(newMenuItem);
        assertNotNull(savedMenuItem);
        assertEquals("Pizza", savedMenuItem.getName());
        assertEquals(10.99, savedMenuItem.getPrice());
        verify(menuItemRepository).save(newMenuItem);
    }

    @Test
    void testGetAllMenuItems_Success() {
        MenuItem item1 = new MenuItem();
        item1.setName("Pizza");
        MenuItem item2 = new MenuItem();
        item2.setName("Burger");

        List<MenuItem> menuItems = Arrays.asList(item1, item2);

        when(menuItemRepository.findAll()).thenReturn(menuItems);

        List<MenuItem> retrievedItems = menuItemService.getAllMenuItems();

        assertNotNull(retrievedItems);
        assertEquals(2, retrievedItems.size());
        verify(menuItemRepository).findAll();
    }

    @Test
    void testGetMenuItemByName_Found() {
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Pizza");
        menuItem.setPrice(10.99);

        when(menuItemRepository.findByNameIgnoreCase("Pizza")).thenReturn(Optional.of(menuItem));
        Optional<MenuItem> foundItem = menuItemService.getMenuItemByName("Pizza");
        assertTrue(foundItem.isPresent());
        assertEquals("Pizza", foundItem.get().getName());
        assertEquals(10.99, foundItem.get().getPrice());
    }

    @Test
    void testGetMenuItemByName_NotFound() {
        when(menuItemRepository.findByNameIgnoreCase("Pasta")).thenReturn(Optional.empty());

        Optional<MenuItem> foundItem = menuItemService.getMenuItemByName("Pasta");
        assertFalse(foundItem.isPresent());
    }

    @Test
    void testUpdateMenuItem_Success() {
        MenuItem existingMenuItem = new MenuItem();
        existingMenuItem.setId(String.valueOf(1L));
        existingMenuItem.setName("OldPizza");
        existingMenuItem.setPrice(9.99);

        MenuItem updatedMenuItem = new MenuItem();
        updatedMenuItem.setName("NewPizza");
        updatedMenuItem.setPrice(12.99);

        when(menuItemRepository.findByNameIgnoreCase("OldPizza")).thenReturn(Optional.of(existingMenuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> {
            MenuItem savedItem = invocation.getArgument(0);
            savedItem.setId(String.valueOf(1L));
            return savedItem;
        });
        MenuItem result = menuItemService.updateMenuItem("OldPizza", updatedMenuItem);
        assertNotNull(result);
        assertEquals("NewPizza", result.getName());
        assertEquals(12.99, result.getPrice());
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_NotFound() {
        MenuItem updatedMenuItem = new MenuItem();
        updatedMenuItem.setName("NewPizza");
        updatedMenuItem.setPrice(12.99);

        when(menuItemRepository.findByNameIgnoreCase("OldPizza")).thenReturn(Optional.empty());
        MenuItem result = menuItemService.updateMenuItem("OldPizza", updatedMenuItem);
        assertNull(result);
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void testDeleteMenuItem_Success() {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(String.valueOf(1L));
        menuItem.setName("Pizza");

        when(menuItemRepository.findByNameIgnoreCase("Pizza")).thenReturn(Optional.of(menuItem));
        doNothing().when(menuItemRepository).deleteById(String.valueOf(1L));
        menuItemService.deleteMenuItem("Pizza");
        verify(menuItemRepository).findByNameIgnoreCase("Pizza");
        verify(menuItemRepository).deleteById(String.valueOf(1L));
    }

    @Test
    void testDeleteMenuItem_NotFound() {
        when(menuItemRepository.findByNameIgnoreCase("Pizza")).thenReturn(Optional.empty());

        menuItemService.deleteMenuItem("Pizza");
        verify(menuItemRepository).findByNameIgnoreCase("Pizza");
        verify(menuItemRepository, never()).deleteById(any());
    }
}