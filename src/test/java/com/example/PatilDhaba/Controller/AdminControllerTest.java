package com.example.PatilDhaba.Controller;

import com.example.PatilDhaba.model.Admin;
import com.example.PatilDhaba.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private Admin admin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        admin = new Admin();
        admin.setUsername("adminUser");
        admin.setPassword("password123");
        admin.setEmail("admin@example.com");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setPhoneNumber("1234567890");
    }

    @Test
    void registerAdmin_ThrowsIllegalArgumentException() {
        doThrow(new IllegalArgumentException("Email already exists")).when(adminService).registerAdmin(admin);

        ResponseEntity<String> response = adminController.registerAdmin(admin);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Email already exists", response.getBody());
        verify(adminService, times(1)).registerAdmin(admin);
    }

    @Test
    void loginAdmin_Success() {
        when(adminService.loginAdmin(admin.getUsername(), admin.getPassword())).thenReturn(admin);

        ResponseEntity<String> response = adminController.loginAdmin(admin);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Admin logged in successfully", response.getBody());
        verify(adminService, times(1)).loginAdmin(admin.getUsername(), admin.getPassword());
    }

    @Test
    void loginAdmin_InvalidCredentials() {
        when(adminService.loginAdmin(admin.getUsername(), admin.getPassword())).thenReturn(null);

        ResponseEntity<String> response = adminController.loginAdmin(admin);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid credentials", response.getBody());
        verify(adminService, times(1)).loginAdmin(admin.getUsername(), admin.getPassword());
    }

    @Test
    void deleteAdmin_Success() {
        doNothing().when(adminService).deleteAdmin(admin.getUsername());

        ResponseEntity<String> response = adminController.deleteAdmin(admin.getUsername());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Admin deleted successfully", response.getBody());
        verify(adminService, times(1)).deleteAdmin(admin.getUsername());
    }

    @Test
    void deleteAdmin_ThrowsIllegalArgumentException() {
        doThrow(new IllegalArgumentException("Admin not found")).when(adminService).deleteAdmin(admin.getUsername());

        ResponseEntity<String> response = adminController.deleteAdmin(admin.getUsername());

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Admin not found", response.getBody());
        verify(adminService, times(1)).deleteAdmin(admin.getUsername());
    }

    @Test
    void updateAdmin_ThrowsIllegalArgumentException() {
        doThrow(new IllegalArgumentException("Invalid current password")).when(adminService).updateAdmin(admin.getUsername(), "password123", admin);

        ResponseEntity<String> response = adminController.updateAdmin(admin.getUsername(), admin, "password123");

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid current password", response.getBody());
        verify(adminService, times(1)).updateAdmin(admin.getUsername(), "password123", admin);
    }
}
