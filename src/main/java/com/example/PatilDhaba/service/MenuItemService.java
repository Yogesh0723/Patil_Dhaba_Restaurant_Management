package com.example.PatilDhaba.service;

import com.example.PatilDhaba.model.MenuItem;
import com.example.PatilDhaba.repository.MenuItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    public MenuItem addMenuItem(MenuItem menuItem) {
        log.info("Added Item");
        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getAllMenuItems() {
        log.info("Fetched Item");
        return menuItemRepository.findAll();
    }

    public Optional<MenuItem> getMenuItemByName(String name) {
        System.out.println("Searching menu item by name (ignore case): " + name); // Debug log
        return menuItemRepository.findByNameIgnoreCase(name);
    }

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

    public void deleteMenuItem(String name) {
        Optional<MenuItem> existingMenuItem = menuItemRepository.findByNameIgnoreCase(name);
        existingMenuItem.ifPresent(menuItem -> menuItemRepository.deleteById(menuItem.getId()));
    }
}