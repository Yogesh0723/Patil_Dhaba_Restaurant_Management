package com.example.PatilDhaba.Controller;

import com.example.PatilDhaba.model.Employee;
import com.example.PatilDhaba.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId("1");
        employee.setName("John Doe");
        employee.setSalaryPerDay(1000);
    }

    @Test
    void testCreateEmployee() {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(employee);

        Employee createdEmployee = employeeController.createEmployee(employee);

        assertNotNull(createdEmployee);
        assertEquals("John Doe", createdEmployee.getName());
        verify(employeeService, times(1)).createEmployee(employee);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        List<Employee> result = employeeController.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testGetEmployeeById() {
        when(employeeService.getEmployeeById(anyString())).thenReturn(Optional.of(employee));

        ResponseEntity<Employee> response = employeeController.getEmployeeById("1");

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("John Doe", response.getBody().getName());
        verify(employeeService, times(1)).getEmployeeById("1");
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeService.getEmployeeById(anyString())).thenReturn(Optional.empty());

        ResponseEntity<Employee> response = employeeController.getEmployeeById("1");

        assertEquals(404, response.getStatusCodeValue());
        verify(employeeService, times(1)).getEmployeeById("1");
    }

    @Test
    void testUpdateEmployee() {
        when(employeeService.updateEmployee(anyString(), any(Employee.class))).thenReturn(employee);

        Employee updatedEmployee = employeeController.updateEmployee("1", employee);

        assertEquals("John Doe", updatedEmployee.getName());
        verify(employeeService, times(1)).updateEmployee("1", employee);
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeService).deleteEmployee(anyString());

        employeeController.deleteEmployee("1");

        verify(employeeService, times(1)).deleteEmployee("1");
    }

    @Test
    void testCalculateMonthlySalary() {
        when(employeeService.calculateMonthlySalary(anyString(), anyInt())).thenReturn(2000.0);

        double salary = employeeController.calculateMonthlySalary("1", 1);

        assertEquals(2000.0, salary);
        verify(employeeService, times(1)).calculateMonthlySalary("1", 1);
    }

    @Test
    void testGetEmployeeAttendance() {
        List<Double> attendance = new ArrayList<>();
        attendance.add(8.0);
        attendance.add(9.0);
        when(employeeService.getEmployeeById(anyString())).thenReturn(Optional.of(employee));
        employee.setAttendance(Map.of(1, attendance));

        ResponseEntity<List<Double>> response = employeeController.getEmployeeAttendance("1", 1);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(attendance, response.getBody());
        verify(employeeService, times(1)).getEmployeeById("1");
    }

    @Test
    void testGetEmployeeAttendance_NotFound() {
        when(employeeService.getEmployeeById(anyString())).thenReturn(Optional.empty());

        ResponseEntity<List<Double>> response = employeeController.getEmployeeAttendance("1", 1);

        assertEquals(404, response.getStatusCodeValue());
        verify(employeeService, times(1)).getEmployeeById("1");
    }

    @Test
    void testUpdateAttendance() {
        doNothing().when(employeeService).updateAttendance(anyString(), anyInt(), anyInt(), anyDouble());

        ResponseEntity<Void> response = employeeController.updateAttendance("1", 1, 1, 8.0);

        assertEquals(200, response.getStatusCodeValue());
        verify(employeeService, times(1)).updateAttendance("1", 1, 1, 8.0);
    }

    @Test
    void testSearchEmployeesByName() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        when(employeeService.searchEmployeesByName(anyString())).thenReturn(employees);

        List<Employee> result = employeeController.searchEmployeesByName("John");

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(employeeService, times(1)).searchEmployeesByName("John");
    }
}