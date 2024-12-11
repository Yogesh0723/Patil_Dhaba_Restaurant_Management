package com.example.PatilDhaba.repository;

import com.example.PatilDhaba.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing employee data in the database.
 * <p>
 * This interface extends {@link MongoRepository} to provide CRUD operations for the Employee entity.
 * </p>
 */
@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    /**
     * Finds employees whose names contain the specified string, ignoring case.
     *
     * @param name the string to search for in employee names
     * @return a list of employees matching the search criteria
     */
    List<Employee> findByNameContainingIgnoreCase(String name);
}