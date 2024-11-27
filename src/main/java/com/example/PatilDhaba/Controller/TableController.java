package com.example.PatilDhaba.Controller;

import com.example.PatilDhaba.model.OrderItem;
import com.example.PatilDhaba.model.Table;
import com.example.PatilDhaba.service.TableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/tables")
public class TableController {

    @Autowired
    private TableService tableService;

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

    @PostMapping("/orderItem/add/{tableNumber}")
    public ResponseEntity<Table> addOrderItem(@PathVariable int tableNumber, @RequestBody OrderItem orderItem) {
        Table table = tableService.addOrderItem(tableNumber, orderItem);
        log.info("Order item added successfully");
        return ResponseEntity.ok(table);
    }

    @PutMapping("/discount/{tableNumber}")
    public ResponseEntity<Table> applyDiscount(@PathVariable int tableNumber, @RequestParam double discount) {
        Table table = tableService.applyDiscount(tableNumber, discount);
        return ResponseEntity.ok(table);
    }

    @DeleteMapping("/clear/{tableNumber}")
    public ResponseEntity<Void> clearTable(@PathVariable int tableNumber) {
        tableService.clearTable(tableNumber);
        return ResponseEntity.noContent().build();
    }

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

    @GetMapping("/profit/today")
    public ResponseEntity<Double> getTodayProfit() {
        double profit = tableService.getTodayProfit();
        return ResponseEntity.ok(profit);
    }

    // Updated endpoint to settle the bill without modifying KOT number
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