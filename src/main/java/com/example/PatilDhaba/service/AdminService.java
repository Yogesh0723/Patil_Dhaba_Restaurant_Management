package com.example.PatilDhaba.service;

import com.example.PatilDhaba.model.Admin;
import com.example.PatilDhaba.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing admin operations.
 * <p>
 * This class provides methods for registering, logging in, updating, and deleting admins,
 * as well as validating credentials and handling password encryption.
 * </p>
 */
@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Registers a new admin.
     *
     * @param admin the admin details to register
     * @return the registered admin
     * @throws IllegalArgumentException if the username or email is already taken
     */
    public Admin registerAdmin(Admin admin) throws IllegalArgumentException {
        if (adminRepository.findByUsername(admin.getUsername()) != null) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (adminRepository.findByEmail(admin.getEmail()) != null) {
            throw new IllegalArgumentException("Email is already registered");
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    /**
     * Logs in an admin with the provided credentials.
     *
     * @param username the admin's username
     * @param password the admin's password
     * @return the logged-in admin if credentials are valid, or null if invalid
     */
    public Admin loginAdmin(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            return admin;
        }
        return null;
    }

    /**
     * Updates an existing admin's details.
     *
     * @param username     the username of the admin to update
     * @param updatedAdmin the new admin details
     * @return the updated admin
     * @throws IllegalArgumentException if the admin is not found or if the new username/email is already taken
     */
    public Admin updateAdmin(String username, Admin updatedAdmin) throws IllegalArgumentException {
        Optional<Admin> existingAdminOpt = Optional.ofNullable(adminRepository.findByUsername(username));
        if (!existingAdminOpt.isPresent()) {
            throw new IllegalArgumentException("Admin not found");
        }

        Admin existingAdmin = existingAdminOpt.get();

        // Check if the new username is different and already taken by another admin
        if (!existingAdmin.getUsername().equals(updatedAdmin.getUsername()) && adminRepository.findByUsername(updatedAdmin.getUsername()) != null) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Check if the new email is different and already taken by another admin
        if (!existingAdmin.getEmail().equals(updatedAdmin.getEmail()) && adminRepository.findByEmail(updatedAdmin.getEmail()) != null) {
            throw new IllegalArgumentException("Email is already registered");
        }

        existingAdmin.setUsername(updatedAdmin.getUsername());
        existingAdmin.setEmail(updatedAdmin.getEmail());
        if (!updatedAdmin.getPassword().isEmpty()) {
            existingAdmin.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));
        }

        return adminRepository.save(existingAdmin);
    }

    /**
     * Deletes an admin by their username.
     *
     * @param username the username of the admin to delete
     * @throws IllegalArgumentException if the admin is not found
     */
    public void deleteAdmin(String username) throws IllegalArgumentException {
        Optional<Admin> existingAdmin = Optional.ofNullable(adminRepository.findByUsername(username));
        if (!existingAdmin.isPresent()) {
            throw new IllegalArgumentException("Admin not found");
        }
        adminRepository.delete(existingAdmin.get());
    }

    /**
     * Updates an existing admin's details with current password validation.
     *
     * @param username        the username of the admin to update
     * @param currentPassword the current password for validation
     * @param updatedAdmin    the new admin details
     * @return the updated admin
     * @throws IllegalArgumentException if the admin is not found or if the current password is incorrect
     */
    public Admin updateAdmin(String username, String currentPassword, Admin updatedAdmin) throws IllegalArgumentException {
        Optional<Admin> existingAdminOpt = Optional.ofNullable(adminRepository.findByUsername(username));
        if (!existingAdminOpt.isPresent()) {
            throw new IllegalArgumentException("Admin not found");
        }

        Admin existingAdmin = existingAdminOpt.get();

        // Validate the current password
        if (!passwordEncoder.matches(currentPassword, existingAdmin.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Check if the new username is different and already taken by another admin
        if (!existingAdmin.getUsername().equals(updatedAdmin.getUsername()) && adminRepository.findByUsername(updatedAdmin.getUsername()) != null) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Check if the new email is different and already taken by another admin
        if (!existingAdmin.getEmail().equals(updatedAdmin.getEmail()) && adminRepository.findByEmail(updatedAdmin.getEmail()) != null) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Update all fields
        existingAdmin.setUsername(updatedAdmin.getUsername());
        existingAdmin.setEmail(updatedAdmin.getEmail());
        existingAdmin.setFirstName(updatedAdmin.getFirstName());
        existingAdmin.setLastName(updatedAdmin.getLastName());
        existingAdmin.setPhoneNumber(updatedAdmin.getPhoneNumber());

        // Update password only if a new one is provided
        if (updatedAdmin.getPassword() != null && !updatedAdmin.getPassword().isEmpty()) {
            existingAdmin.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));
        }

        return adminRepository.save(existingAdmin);
    }
}