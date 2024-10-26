package com.example.PatilDhaba.service;

import com.example.PatilDhaba.model.OrderItem;
import com.example.PatilDhaba.model.Table;
import com.example.PatilDhaba.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TableService {

    @Autowired
    private TableRepository tableRepository;

    public Table createTable(int tableNumber) {
        Optional<Table> tableOpt = tableRepository.findByTableNumber(tableNumber);
        if (tableOpt.isPresent()) {
            throw new IllegalArgumentException("Table already exists");
        }
        Table table = new Table();
        table.setTableNumber(tableNumber);
        return tableRepository.save(table);
    }

    public Table addOrderItem(int tableNumber, OrderItem orderItem) {
        Optional<Table> tableOpt = tableRepository.findByTableNumber(tableNumber);
        Table table;
        if (tableOpt.isPresent()) {
            table = tableOpt.get();
            System.out.println("Found existing table: " + tableNumber);
        } else {
            table = new Table();
            table.setTableNumber(tableNumber);
            System.out.println("Creating new table: " + tableNumber);
        }

        orderItem.setOrderTime(LocalDateTime.now());
        table.getOrderItems().add(orderItem);
        table.setTotalAmount(table.getTotalAmount() + (orderItem.getPrice() * orderItem.getQuantity()));
        return tableRepository.save(table);
    }

    public Table applyDiscount(int tableNumber, double discount) {
        Optional<Table> tableOpt = tableRepository.findByTableNumber(tableNumber);
        if (!tableOpt.isPresent()) {
            throw new IllegalArgumentException("Table not found");
        }

        Table table = tableOpt.get();
        table.setDiscount(discount);
        return tableRepository.save(table);
    }

    public void clearTable(int tableNumber) {
        Optional<Table> tableOpt = tableRepository.findByTableNumber(tableNumber);
        if (tableOpt.isPresent()) {
            Table table = tableOpt.get();
            table.getOrderItems().clear();
            table.setTotalAmount(0);
            table.setDiscount(0);
            tableRepository.save(table);
        }
    }

    public Table getTable(int tableNumber) {
        return tableRepository.findByTableNumber(tableNumber).orElse(null);
    }

    public double getTodayProfit() {
        LocalDate today = LocalDate.now();
        List<Table> tables = tableRepository.findAll();
        return tables.stream()
                .flatMap(table -> table.getOrderItems().stream())
                .filter(orderItem -> orderItem.getOrderTime().toLocalDate().isEqual(today))
                .mapToDouble(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                .sum();
    }
}