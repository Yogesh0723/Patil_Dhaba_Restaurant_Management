package com.example.PatilDhaba.Controller;

import com.example.PatilDhaba.model.Admin;
import com.example.PatilDhaba.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing admin operations.
 * <p>
 * This class provides RESTful endpoints for admin registration, login, deletion, and updating of admin details.
 * It uses the {@link AdminService} to perform the necessary operations and logs the actions taken.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    /**
     * Registers a new admin.
     *
     * @param admin the admin details to register
     * @return a response entity indicating the result of the registration
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestBody Admin admin) {
        try {
            adminService.registerAdmin(admin);
            log.info("Admin registered successfully");
            return ResponseEntity.ok("Admin registered successfully");
        } catch (IllegalArgumentException e) {
            log.warn("Registration error: " + e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * Logs in an existing admin.
     *
     * @param admin the admin credentials for login
     * @return a response entity indicating the result of the login attempt
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginAdmin(@RequestBody Admin admin) {
        Admin foundAdmin = adminService.loginAdmin(admin.getUsername(), admin.getPassword());
        if (foundAdmin != null) {
            log.info("Admin logged in successfully");
            return ResponseEntity.ok("Admin logged in successfully");
        }
        log.warn("Invalid credentials");
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    /**
     * Deletes an admin by username.
     *
     * @param username the username of the admin to delete
     * @return a response entity indicating the result of the deletion
     */
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteAdmin(@PathVariable String username) {
        try {
            adminService.deleteAdmin(username);
            log.info("Admin deleted successfully");
            return ResponseEntity.ok("Admin deleted successfully");
        } catch (IllegalArgumentException e) {
            log.warn("Error deleting admin: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * Updates the details of an existing admin.
     *
     * @param username        the username of the admin to update
     * @param admin          the new admin details
     * @param currentPassword the current password of the admin for verification
     * @return a response entity indicating the result of the update
     */
    @PutMapping("/update/{username}")
    public ResponseEntity<String> updateAdmin(@PathVariable String username, @RequestBody Admin admin, @RequestParam String currentPassword) {
        try {
            adminService.updateAdmin(username, currentPassword, admin);
            log.info("Admin details updated successfully");
            return ResponseEntity.ok("Admin details updated successfully");
        } catch (IllegalArgumentException e) {
            log.warn("Error updating admin details: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}