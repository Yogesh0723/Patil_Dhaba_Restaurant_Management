package com.example.PatilDhaba.repository;

import com.example.PatilDhaba.model.Table;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends MongoRepository<Table, String> {
    Optional<Table> findByTableNumber(int tableNumber);
    Optional<Table> findByKotNumber(int kotNumber);

    // New method to find the maximum KOT number
    @Query(value = "{}", fields = "{ 'kotNumber' : 1 }")
    List<Table> findAllKotNumbers();
}