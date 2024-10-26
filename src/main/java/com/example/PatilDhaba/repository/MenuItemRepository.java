package com.example.PatilDhaba.repository;

import com.example.PatilDhaba.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    Optional<MenuItem> findByNameIgnoreCase(String name);
}