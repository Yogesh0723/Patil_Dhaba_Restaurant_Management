package com.example.PatilDhaba.repository;

import com.example.PatilDhaba.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Admin findByUsername(String username);
    Admin findByEmail(String email);
}