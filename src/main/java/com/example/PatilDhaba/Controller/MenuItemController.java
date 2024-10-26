package com.example.PatilDhaba.Controller;

import com.example.PatilDhaba.model.MenuItem;
import com.example.PatilDhaba.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/menu")
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    @PostMapping("/add")
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
        MenuItem createdMenuItem = menuItemService.addMenuItem(menuItem);
        return ResponseEntity.ok(createdMenuItem);
    }

    @GetMapping("/items")
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/item/{name}")
    public ResponseEntity<MenuItem> getMenuItemByName(@PathVariable String name) {
        System.out.println("Fetching item by name: " + name); // Debug log
        Optional<MenuItem> menuItem = menuItemService.getMenuItemByName(name);
        return menuItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{name}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable String name, @RequestBody MenuItem menuItem) {
        MenuItem updatedMenuItem = menuItemService.updateMenuItem(name, menuItem);
        if (updatedMenuItem != null) {
            return ResponseEntity.ok(updatedMenuItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable String name) {
        menuItemService.deleteMenuItem(name);
        return ResponseEntity.noContent().build();
    }
}