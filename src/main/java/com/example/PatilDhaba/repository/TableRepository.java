package com.example.PatilDhaba.repository;

import com.example.PatilDhaba.model.Table;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableRepository extends MongoRepository<Table, String> {
    Optional<Table> findByTableNumber(int tableNumber);
}