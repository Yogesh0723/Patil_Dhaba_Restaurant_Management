package com.example.PatilDhaba.service;

import com.example.PatilDhaba.model.MenuItem;
import com.example.PatilDhaba.repository.MenuItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing menu item operations.
 * <p>
 * This class provides methods for adding, retrieving, updating, and deleting menu items.
 * </p>
 */
@Slf4j
@Service
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    /**
     * Adds a new menu item.
     *
     * @param menuItem the menu item to add
     * @return the added menu item
     */
    public MenuItem addMenuItem(MenuItem menuItem) {
        log.info("Added Item");
        return menuItemRepository.save(menuItem);
    }

    /**
     * Retrieves all menu items.
     *
     * @return a list of all menu items
     */
    public List<MenuItem> getAllMenuItems() {
        log.info("Fetched Item");
        return menuItemRepository.findAll();
    }

    /**
     * Retrieves a menu item by its name, ignoring case.
     *
     * @param name the name of the menu item to search for
     * @return an optional containing the menu item if found, or empty if not found
     */
    public Optional<MenuItem> getMenuItemByName(String name) {
        System.out.println("Searching menu item by name (ignore case): " + name); // Debug log
        return menuItemRepository.findByNameIgnoreCase(name);
    }

    /**
     * Updates an existing menu item.
     *
     * @param name     the name of the menu item to update
     * @param menuItem the new menu item details
     * @return the updated menu item, or null if not found
     */
    public MenuItem updateMenuItem(String name, MenuItem menuItem) {
        Optional<MenuItem> existingMenuItem = menuItemRepository.findByNameIgnoreCase(name);
        if (existingMenuItem.isPresent()) {
            menuItem.setId(existingMenuItem.get().getId()); // Ensure the same ID is used
            log.info("Item Updated");
            return menuItemRepository.save(menuItem);
        } else {
            return null;
        }
    }

    /**
     * Deletes a menu item by its name.
     *
     * @param name the name of the menu item to delete
     */
    public void deleteMenuItem(String name) {
        Optional<MenuItem> existingMenuItem = menuItemRepository.findByNameIgnoreCase(name);
        existingMenuItem.ifPresent(menuItem -> menuItemRepository.deleteById(menuItem.getId()));
    }
}