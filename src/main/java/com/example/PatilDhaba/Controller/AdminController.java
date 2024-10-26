package com.example.PatilDhaba.Controller;

import com.example.PatilDhaba.model.Admin;
import com.example.PatilDhaba.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

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

    @PutMapping("/update/{username}")
    public ResponseEntity<String> updateAdmin(@PathVariable String username, @RequestBody Admin admin) {
        try {
            adminService.updateAdmin(username, admin);
            log.info("Admin details updated successfully");
            return ResponseEntity.ok("Admin details updated successfully");
        } catch (IllegalArgumentException e) {
            log.warn("Error updating admin details: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

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
}