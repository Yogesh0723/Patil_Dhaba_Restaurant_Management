package com.example.PatilDhaba.service;

import com.example.PatilDhaba.model.Employee;
import com.example.PatilDhaba.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
            employee.setAttendance(new HashMap<>());
            for (int month = 1; month <= 12; month++) {
                int daysInMonth = java.time.Month.of(month).length(false); // Non-leap year
                employee.getAttendance().put(month, new ArrayList<>(Collections.nCopies(daysInMonth, 0.0)));
            }
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
            employee.setAttendance(new HashMap<>());
            for (int month = 1; month <= 12; month++) {
                int daysInMonth = java.time.Month.of(month).length(false); // Non-leap year
                employee.getAttendance().put(month, new ArrayList<>(Collections.nCopies(daysInMonth, 0.0)));
            }
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
     * @param id    the ID of the employee
     * @param month the month for which to calculate the salary
     * @return the calculated monthly salary
     */
    public double calculateMonthlySalary(String id, int month) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();

            // Initialize attendance if null
            if (employee.getAttendance() == null || !employee.getAttendance().containsKey(month)) {
                employee.setAttendance(new HashMap<>());
                int daysInMonth = java.time.Month.of(month).length(false); // Non-leap year
                employee.getAttendance().put(month, new ArrayList<>(Collections.nCopies(daysInMonth, 0.0)));
            }

            double salary = 0;
            for (double hours : employee.getAttendance().get(month)) {
                if (hours >= 9) {
                    salary += employee.getSalaryPerDay(); // Full day salary
                } else if (hours > 0) {
                    salary += (employee.getSalaryPerDay() / 9) * hours; // Proportional salary
                }
            }
            employee.setSalaryPerMonth(Math.round(salary));
            employeeRepository.save(employee);
            return Math.round(salary);
        }
        throw new IllegalArgumentException("Employee not found with id: " + id);
    }

    /**
     * Updates the attendance for an employee on a specific day of a specific month.
     *
     * @param id     the ID of the employee
     * @param month  the month (1-12) to update attendance
     * @param day    the day of the month (1-31) to update attendance
     * @param hours  the number of hours worked on that day
     */
    public void updateAttendance(String id, int month, int day, Number hours) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            if (employee.getAttendance() == null) {
                employee.setAttendance(new HashMap<>());
            }
            employee.getAttendance().putIfAbsent(month, new ArrayList<>(Collections.nCopies(31, 0.0)));

            if (day < 1 || day > 31) {
                throw new IllegalArgumentException("Day must be between 1 and 31");
            }

            employee.getAttendance().get(month).set(day - 1, hours.doubleValue());
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