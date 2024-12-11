package com.example.PatilDhaba.repository;

import com.example.PatilDhaba.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing menu item data in the database.
 * <p>
 * This interface extends {@link MongoRepository} to provide CRUD operations for the MenuItem entity.
 * </p>
 */
@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    /**
     * Finds a menu item by its name, ignoring case.
     *
     * @param name the name of the menu item
     * @return an optional containing the menu item if found, or empty if not found
     */
    Optional<MenuItem> findByNameIgnoreCase(String name);
}