package com.example.PatilDhaba.repository;

import com.example.PatilDhaba.model.Table;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing table data in the database.
 * <p>
 * This interface extends {@link MongoRepository} to provide CRUD operations for the Table entity.
 * </p>
 */
@Repository
public interface TableRepository extends MongoRepository<Table, String> {
    /**
     * Finds a table by its number.
     *
     * @param tableNumber the number of the table
     * @return an optional containing the table if found, or empty if not found
     */
    Optional<Table> findByTableNumber(int tableNumber);

    /**
     * Finds a table by its KOT number.
     *
     * @param kotNumber the KOT number of the table
     * @return an optional containing the table if found, or empty if not found
     */
    Optional<Table> findByKotNumber(int kotNumber);

    /**
     * Finds all KOT numbers.
     *
     * @return a list of tables containing their KOT numbers
     */
    @Query(value = "{}", fields = "{ 'kotNumber' : 1 }")
    List<Table> findAllKotNumbers();
}