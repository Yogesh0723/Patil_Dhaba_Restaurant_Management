package com.example.PatilDhaba.service;

import com.example.PatilDhaba.model.Employee;
import com.example.PatilDhaba.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing employee operations.
 * <p>
 * This class provides methods for creating, retrieving, updating, and deleting employees,
 * as well as calculating salaries and managing attendance.
 * </p>
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Creates a new employee.
     *
     * @param employee the employee details to create
     * @return the created employee
     */
    public Employee createEmployee(Employee employee) {
        // Ensure attendance is initialized
        if (employee.getAttendance() == null || employee.getAttendance().isEmpty()) {
            employee.setAttendance(new ArrayList<>(Collections.nCopies(31, 0.0)));
        }
        return employeeRepository.save(employee);
    }

    /**
     * Retrieves all employees.
     *
     * @return a list of all employees
     */
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * Retrieves an employee by their ID.
     *
     * @param id the ID of the employee to retrieve
     * @return an optional containing the employee if found, or empty if not found
     */
    public Optional<Employee> getEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    /**
     * Updates an existing employee.
     *
     * @param id       the ID of the employee to update
     * @param employee the new employee details
     * @return the updated employee
     */
    public Employee updateEmployee(String id, Employee employee) {
        employee.setId(id);
        // Ensure attendance is initialized
        if (employee.getAttendance() == null || employee.getAttendance().isEmpty()) {
            employee.setAttendance(new ArrayList<>(Collections.nCopies(31, 0.0)));
        }
        return employeeRepository.save(employee);
    }

    /**
     * Deletes an employee by their ID.
     *
     * @param id the ID of the employee to delete
     */
    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }

    /**
     * Calculates the monthly salary for an employee.
     *
     * @param id the ID of the employee
     * @return the calculated monthly salary
     */
    public double calculateMonthlySalary(String id) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();

            // Initialize attendance if null
            if (employee.getAttendance() == null) {
                employee.setAttendance(new ArrayList<>(Collections.nCopies(31, 0.0)));
            }

            double salary = 0;
            for (double hours : employee.getAttendance()) {
                if (hours >= 9) {
                    salary += employee.getSalaryPerDay(); // Full day salary
                } else if (hours > 0) {
                    salary += (employee.getSalaryPerDay() / 9) * hours; // Proportional salary
                }
            }
            employee.setSalaryPerMonth(salary);
            employeeRepository.save(employee);

            System.out.println("Calculated salary for employee ID " + id + ": " + salary);
            return salary;
        }
        throw new IllegalArgumentException("Employee not found with id: " + id);
    }

    /**
     * Updates the attendance for an employee on a specific day.
     *
     * @param id     the ID of the employee
     * @param day    the day of the month (1-31) to update attendance
     * @param hours  the number of hours worked on that day
     */
    public void updateAttendance(String id, int day, Number hours) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            if (employee.getAttendance() == null || employee.getAttendance().size() < 31) {
                employee.setAttendance(new ArrayList<>(Collections.nCopies(31, 0.0)));
            }
            if (day < 1 || day > 31) {
                throw new IllegalArgumentException("Day must be between 1 and 31");
            }

            employee.getAttendance().set(day - 1, hours.doubleValue());
            employeeRepository.save(employee);
        } else {
            throw new IllegalArgumentException("Employee not found with id: " + id);
        }
    }

    /**
     * Searches for employees by name.
     *
     * @param name the name to search for
     * @return a list of employees matching the search criteria
     */
    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository.findByNameContainingIgnoreCase(name);
    }
}