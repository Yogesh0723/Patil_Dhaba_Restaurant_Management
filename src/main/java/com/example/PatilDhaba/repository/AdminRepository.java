package com.example.PatilDhaba.repository;

import com.example.PatilDhaba.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for managing admin data in the database.
 * <p>
 * This interface extends {@link MongoRepository} to provide CRUD operations for the Admin entity.
 * </p>
 */
public interface AdminRepository extends MongoRepository<Admin, String> {
    /**
     * Finds an admin by their username.
     *
     * @param username the username of the admin
     * @return the admin with the specified username
     */
    Admin findByUsername(String username);

    /**
     * Finds an admin by their email.
     *
     * @param email the email of the admin
     * @return the admin with the specified email
     */
    Admin findByEmail(String email);
}