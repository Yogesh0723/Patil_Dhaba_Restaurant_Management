package com.example.PatilDhaba.Controller;

import com.example.PatilDhaba.model.OrderItem;
import com.example.PatilDhaba.model.Table;
import com.example.PatilDhaba.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tables")
public class TableController {

    @Autowired
    private TableService tableService;

    @PostMapping("/create")
    public ResponseEntity<Table> createTable(@RequestParam int tableNumber) {
        try {
            Table table = tableService.createTable(tableNumber);
            return ResponseEntity.ok(table);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/orderItem/add/{tableNumber}")
    public ResponseEntity<Table> addOrderItem(@PathVariable int tableNumber, @RequestBody OrderItem orderItem) {
        Table table = tableService.addOrderItem(tableNumber, orderItem);
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
            return ResponseEntity.ok(table);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/profit/today")
    public ResponseEntity<Double> getTodayProfit() {
        double profit = tableService.getTodayProfit();
        return ResponseEntity.ok(profit);
    }
}