package com.example.PatilDhaba.Controller;

import com.example.PatilDhaba.model.MenuItem;
import com.example.PatilDhaba.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for managing menu items.
 * <p>
 * This class provides RESTful endpoints for adding, retrieving, updating, and deleting menu items.
 * </p>
 */
@RestController
@RequestMapping("/menu")
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    /**
     * Adds a new menu item.
     *
     * @param menuItem the menu item to add
     * @return the created menu item
     */
    @PostMapping("/add")
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
        MenuItem createdMenuItem = menuItemService.addMenuItem(menuItem);
        return ResponseEntity.ok(createdMenuItem);
    }

    /**
     * Retrieves all menu items.
     *
     * @return a list of all menu items
     */
    @GetMapping("/items")
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
        return ResponseEntity.ok(menuItems);
    }

    /**
     * Retrieves a menu item by its name.
     *
     * @param name the name of the menu item
     * @return a response entity containing the menu item if found, or a not found response
     */
    @GetMapping("/item/{name}")
    public ResponseEntity<MenuItem> getMenuItemByName(@PathVariable String name) {
        Optional<MenuItem> menuItem = menuItemService.getMenuItemByName(name);
        return menuItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing menu item.
     *
     * @param name     the name of the menu item to update
     * @param menuItem the new menu item details
     * @return a response entity containing the updated menu item
     */
    @PutMapping("/update/{name}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable String name, @RequestBody MenuItem menuItem) {
        MenuItem updatedMenuItem = menuItemService.updateMenuItem(name, menuItem);
        if (updatedMenuItem != null) {
            return ResponseEntity.ok(updatedMenuItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a menu item by its name.
     *
     * @param name the name of the menu item to delete
     * @return a response entity indicating the result of the deletion
     */
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable String name) {
        menuItemService.deleteMenuItem(name);
        return ResponseEntity.noContent().build();
    }
}