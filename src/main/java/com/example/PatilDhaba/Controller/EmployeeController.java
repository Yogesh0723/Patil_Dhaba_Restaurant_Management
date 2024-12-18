package com.example.PatilDhaba.Controller;

import com.example.PatilDhaba.model.Employee;
import com.example.PatilDhaba.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing employee operations.
 * <p>
 * This class provides RESTful endpoints for creating, retrieving, updating, and deleting employee records.
 * It also includes endpoints for calculating monthly salary and managing employee attendance.
 * </p>
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Creates a new employee.
     *
     * @param employee the employee details to create
     * @return the created employee
     */
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    /**
     * Retrieves all employees.
     *
     * @return a list of all employees
     */
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    /**
     * Retrieves an employee by their ID.
     *
     * @param id the ID of the employee to retrieve
     * @return a response entity containing the employee if found, or a not found response
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing employee.
     *
     * @param id       the ID of the employee to update
     * @param employee the new employee details
     * @return the updated employee
     */
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable String id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    /**
     * Deletes an employee by their ID.
     *
     * @param id the ID of the employee to delete
     */
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployee(id);
    }

    /**
     * Calculates the monthly salary for an employee for a specific month.
     *
     * @param id    the ID of the employee
     * @param month the month for which to calculate the salary (1-12)
     * @return the calculated monthly salary
     */
    @GetMapping("/{id}/salary/{month}")
    public double calculateMonthlySalary(@PathVariable String id, @PathVariable int month) {
        double salary = employeeService.calculateMonthlySalary(id, month);
        System.out.println("Calculated salary for employee ID " + id + " for month " + month + ": " + salary);
        return salary;
    }

    /**
     * Retrieves the attendance records for an employee for a specific month.
     *
     * @param id    the ID of the employee
     * @param month the month for which to retrieve attendance records (1-12)
     * @return a response entity containing the attendance records if found, or a not found response
     */
        @GetMapping("/{id}/attendance/{month}")
    public ResponseEntity<List<Double>> getEmployeeAttendance(@PathVariable String id, @PathVariable int month) {
        return employeeService.getEmployeeById(id)
                .map(employee -> ResponseEntity.ok(employee.getAttendance().get(month)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates the attendance for an employee on a specific day of a specific month.
     *
     * @param id     the ID of the employee
     * @param month  the month (1-12) to update attendance
     * @param day    the day of the month (1-31) to update attendance
     * @param hours  the number of hours worked on that day
     * @return a response entity indicating the result of the update
     */
    @PutMapping("/{id}/attendance/{month}/{day}")
    public ResponseEntity<Void> updateAttendance(
            @PathVariable String id,
            @PathVariable int month,
            @PathVariable int day,
            @RequestParam double hours
    ) {
        employeeService.updateAttendance(id, month, day, hours);
        return ResponseEntity.ok().build();
    }

    /**
     * Searches for employees by name.
     *
     * @param name the name to search for
     * @return a list of employees matching the search criteria
     */
    @GetMapping("/search")
    public List<Employee> searchEmployeesByName(@RequestParam String name) {
        return employeeService.searchEmployeesByName(name);
    }
}