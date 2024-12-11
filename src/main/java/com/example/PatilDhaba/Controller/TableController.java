package com.example.PatilDhaba.Controller;

import com.example.PatilDhaba.model.OrderItem;
import com.example.PatilDhaba.model.Table;
import com.example.PatilDhaba.service.TableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing tables in the restaurant.
 * <p>
 * This class provides RESTful endpoints for creating tables, adding order items, applying discounts,
 * clearing tables, fetching table details, and settling bills.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/tables")
public class TableController {

    @Autowired
    private TableService tableService;

    /**
     * Creates a new table.
     *
     * @param tableNumber the number of the table to create
     * @return a response entity containing the created table
     */
    @PostMapping("/create")
    public ResponseEntity<Table> createTable(@RequestParam int tableNumber) {
        try {
            Table table = tableService.createTable(tableNumber);
            log.info("Table created or already exists: {}", tableNumber);
            return ResponseEntity.ok(table);
        } catch (IllegalArgumentException ex) {
            log.warn("Error creating table: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Adds an order item to a specific table.
     *
     * @param tableNumber the number of the table
     * @param orderItem   the order item to add
     * @return a response entity containing the updated table
     */
    @PostMapping("/orderItem/add/{tableNumber}")
    public ResponseEntity<Table> addOrderItem(@PathVariable int tableNumber, @RequestBody OrderItem orderItem) {
        Table table = tableService.addOrderItem(tableNumber, orderItem);
        log.info("Order item added successfully");
        return ResponseEntity.ok(table);
    }

    /**
     * Applies a discount to a specific table.
     *
     * @param tableNumber the number of the table
     * @param discount    the discount amount to apply
     * @return a response entity containing the updated table
     */
    @PutMapping("/discount/{tableNumber}")
    public ResponseEntity<Table> applyDiscount(@PathVariable int tableNumber, @RequestParam double discount) {
        Table table = tableService.applyDiscount(tableNumber, discount);
        return ResponseEntity.ok(table);
    }

    /**
     * Clears a specific table.
     *
     * @param tableNumber the number of the table to clear
     * @return a response entity indicating the result of the operation
     */
    @DeleteMapping("/clear/{tableNumber}")
    public ResponseEntity<Void> clearTable(@PathVariable int tableNumber) {
        tableService.clearTable(tableNumber);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a specific table by its number.
     *
     * @param tableNumber the number of the table to retrieve
     * @return a response entity containing the table if found, or a not found response
     */
    @GetMapping("/{tableNumber}")
    public ResponseEntity<Table> getTable(@PathVariable int tableNumber) {
        Table table = tableService.getTable(tableNumber);
        if (table != null) {
            log.info("Table fetched successfully");
            return ResponseEntity.ok(table);
        } else {
            log.warn("Table not found");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves today's profit.
     *
     * @return a response entity containing today's profit
     */
    @GetMapping("/profit/today")
    public ResponseEntity<Double> getTodayProfit() {
        double profit = tableService.getTodayProfit();
        return ResponseEntity.ok(profit);
    }

    /**
     * Settles the bill for a specific table.
     *
     * @param tableNumber the number of the table to settle the bill for
     * @return a response entity indicating the result of the operation
     */
    @PostMapping("/settle/{tableNumber}")
    public ResponseEntity<Void> settleBill(@PathVariable int tableNumber) {
        try {
            tableService.settleBill(tableNumber);
            log.info("Bill settled successfully for table {}", tableNumber);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            log.warn("Error settling bill: {}", ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}