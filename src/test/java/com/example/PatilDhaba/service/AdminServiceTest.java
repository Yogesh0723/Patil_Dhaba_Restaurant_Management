package com.example.PatilDhaba.service;

import com.example.PatilDhaba.model.Admin;
import com.example.PatilDhaba.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterAdmin_Success() {
        // Arrange
        Admin newAdmin = new Admin();
        newAdmin.setUsername("newadmin");
        newAdmin.setEmail("newadmin@example.com");
        newAdmin.setPassword("password123");

        when(adminRepository.findByUsername(newAdmin.getUsername())).thenReturn(null);
        when(adminRepository.findByEmail(newAdmin.getEmail())).thenReturn(null);
        when(adminRepository.save(any(Admin.class))).thenReturn(newAdmin);

        // Act
        Admin registeredAdmin = adminService.registerAdmin(newAdmin);

        // Assert
        assertNotNull(registeredAdmin);
        verify(adminRepository).save(any(Admin.class));
        assertTrue(passwordEncoder.matches("password123", registeredAdmin.getPassword()));
    }

    @Test
    void testRegisterAdmin_UsernameAlreadyTaken() {
        Admin existingAdmin = new Admin();
        existingAdmin.setUsername("existingadmin");
        existingAdmin.setEmail("existing@example.com");

        Admin newAdmin = new Admin();
        newAdmin.setUsername("existingadmin");
        newAdmin.setEmail("new@example.com");

        when(adminRepository.findByUsername(newAdmin.getUsername())).thenReturn(existingAdmin);
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.registerAdmin(newAdmin);
        }, "Username is already taken");
    }

    @Test
    void testRegisterAdmin_EmailAlreadyRegistered() {
        Admin existingAdmin = new Admin();
        existingAdmin.setUsername("existinguser");
        existingAdmin.setEmail("existing@example.com");

        Admin newAdmin = new Admin();
        newAdmin.setUsername("newadmin");
        newAdmin.setEmail("existing@example.com");

        when(adminRepository.findByUsername(newAdmin.getUsername())).thenReturn(null);
        when(adminRepository.findByEmail(newAdmin.getEmail())).thenReturn(existingAdmin);
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.registerAdmin(newAdmin);
        }, "Email is already registered");
    }

    @Test
    void testLoginAdmin_Success() {
        String username = "testuser";
        String password = "password123";
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));

        when(adminRepository.findByUsername(username)).thenReturn(admin);
        Admin loggedInAdmin = adminService.loginAdmin(username, password);
        assertNotNull(loggedInAdmin);
        assertEquals(username, loggedInAdmin.getUsername());
    }

    @Test
    void testLoginAdmin_Failure() {
        String username = "testuser";
        String password = "wrongpassword";

        when(adminRepository.findByUsername(username)).thenReturn(null);
        Admin loggedInAdmin = adminService.loginAdmin(username, password);
        assertNull(loggedInAdmin);
    }
    @Test
    void testUpdateAdmin_Success() {
        String existingUsername = "olduser";
        Admin existingAdmin = new Admin();
        existingAdmin.setUsername(existingUsername);
        existingAdmin.setEmail("old@example.com");
        existingAdmin.setPassword(passwordEncoder.encode("oldpassword"));

        Admin updatedAdmin = new Admin();
        updatedAdmin.setUsername("newuser");
        updatedAdmin.setEmail("new@example.com");
        updatedAdmin.setPassword("newpassword");

        // Mock the repository methods
        when(adminRepository.findByUsername(existingUsername)).thenReturn(existingAdmin);
        when(adminRepository.findByUsername(updatedAdmin.getUsername())).thenReturn(null);
        when(adminRepository.findByEmail(updatedAdmin.getEmail())).thenReturn(null);
        when(adminRepository.save(any(Admin.class))).thenAnswer(invocation -> {
            Admin savedAdmin = invocation.getArgument(0);
            return savedAdmin;
        });
        Admin resultAdmin = adminService.updateAdmin(existingUsername, updatedAdmin);
        assertNotNull(resultAdmin);
        assertEquals("newuser", resultAdmin.getUsername());
        assertEquals("new@example.com", resultAdmin.getEmail());
        assertTrue(passwordEncoder.matches("newpassword", resultAdmin.getPassword()),
                "Password should be correctly encoded");
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void testUpdateAdmin_WithCurrentPassword_Success() {
        String username = "testuser";
        String currentPassword = "oldpassword";
        Admin existingAdmin = new Admin();
        existingAdmin.setUsername(username);
        existingAdmin.setEmail("old@example.com");
        existingAdmin.setPassword(passwordEncoder.encode(currentPassword));

        Admin updatedAdmin = new Admin();
        updatedAdmin.setUsername("newuser");
        updatedAdmin.setEmail("new@example.com");
        updatedAdmin.setPassword("newpassword");
        when(adminRepository.findByUsername(username)).thenReturn(existingAdmin);
        when(adminRepository.findByUsername(updatedAdmin.getUsername())).thenReturn(null);
        when(adminRepository.findByEmail(updatedAdmin.getEmail())).thenReturn(null);

        when(adminRepository.save(any(Admin.class))).thenAnswer(invocation -> {
            Admin savedAdmin = invocation.getArgument(0);
            return savedAdmin;
        });
        Admin resultAdmin = adminService.updateAdmin(username, currentPassword, updatedAdmin);
        assertNotNull(resultAdmin);
        assertEquals("newuser", resultAdmin.getUsername());
        assertEquals("new@example.com", resultAdmin.getEmail());
        assertTrue(passwordEncoder.matches("newpassword", resultAdmin.getPassword()),
                "Password should be correctly encoded");
        verify(adminRepository).save(any(Admin.class));
    }
    @Test
    void testDeleteAdmin_Success() {
        String username = "testuser";
        Admin existingAdmin = new Admin();
        existingAdmin.setUsername(username);

        when(adminRepository.findByUsername(username)).thenReturn(existingAdmin);
        doNothing().when(adminRepository).delete(existingAdmin);

        assertDoesNotThrow(() -> {
            adminService.deleteAdmin(username);
        });

        verify(adminRepository).delete(existingAdmin);
    }

    @Test
    void testDeleteAdmin_NotFound() {
        String username = "nonexistentuser";

        when(adminRepository.findByUsername(username)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.deleteAdmin(username);
        }, "Admin not found");
    }
}