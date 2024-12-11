package com.example.PatilDhaba.service;

import com.example.PatilDhaba.model.Employee;
import com.example.PatilDhaba.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId("1");
        employee.setName("John Doe");
        employee.setSalaryPerDay(1000);
        employee.setAttendance(new HashMap<>());
    }

    @Test
    void testCreateEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee createdEmployee = employeeService.createEmployee(employee);

        assertNotNull(createdEmployee);
        assertEquals("John Doe", createdEmployee.getName());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetEmployeeById() {
        when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById("1");

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(employeeRepository, times(1)).findById("1");
    }

    @Test
    void testUpdateEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employee.setName("Jane Doe");
        Employee updatedEmployee = employeeService.updateEmployee("1", employee);

        assertEquals("Jane Doe", updatedEmployee.getName());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(anyString());

        employeeService.deleteEmployee("1");

        verify(employeeRepository, times(1)).deleteById("1");
    }

    @Test
    void testCalculateMonthlySalary() {
        employee.setAttendance(Map.of(
                1, Arrays.asList(9.0, 8.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        ));
        when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        double salary = employeeService.calculateMonthlySalary("1", 1);

        assertEquals(1889.0, salary);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testUpdateAttendance() {
        when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.updateAttendance("1", 1, 1, 8);

        assertEquals(8.0, employee.getAttendance().get(1).get(0));
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testSearchEmployeesByName() {
        List<Employee> employees = Collections.singletonList(employee);
        when(employeeRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(employees);

        List<Employee> result = employeeService.searchEmployeesByName("John");

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(employeeRepository, times(1)).findByNameContainingIgnoreCase("John");
    }

    @Test
    void testCalculateMonthlySalary_EmployeeNotFound() {
        when(employeeRepository.findById(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.calculateMonthlySalary("1", 1);
        });

        assertEquals("Employee not found with id: 1", exception.getMessage());
    }

    @Test
    void testUpdateAttendance_EmployeeNotFound() {
        when(employeeRepository.findById(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.updateAttendance("1", 1, 1, 8);
        });

        assertEquals("Employee not found with id: 1", exception.getMessage());
    }
}