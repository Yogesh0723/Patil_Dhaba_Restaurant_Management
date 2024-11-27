package com.example.PatilDhaba.service;

import com.example.PatilDhaba.model.OrderItem;
import com.example.PatilDhaba.model.Table;
import com.example.PatilDhaba.model.MenuItem;
import com.example.PatilDhaba.repository.MenuItemRepository;
import com.example.PatilDhaba.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TableService {

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @PostConstruct
    public void init() {
        for (int i = 1; i <= 30; i++) {
            createTable(i);
        }
    }

    public Table createTable(int tableNumber) {
        Optional<Table> existingTableOpt = tableRepository.findByTableNumber(tableNumber);
        if (existingTableOpt.isPresent()) {
            return existingTableOpt.get(); // Return existing table details
        }

        // Fetch the highest KOT number from the database
        int maxKotNumber = tableRepository.findAllKotNumbers().stream()
                .mapToInt(Table::getKotNumber)
                .max()
                .orElse(0); // Default to 0 if no tables exist

        Table table = new Table();
        table.setTableNumber(tableNumber);
        table.setKotNumber(maxKotNumber + 1); // Set KOT number to highest + 1
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

        // Fetch the price from the MenuItem repository
        Optional<MenuItem> menuItemOpt = menuItemRepository.findByNameIgnoreCase(orderItem.getMenuItemName());
        if (menuItemOpt.isPresent()) {
            orderItem.setPrice(menuItemOpt.get().getPrice());
        } else {
            throw new IllegalArgumentException("Menu item not found: " + orderItem.getMenuItemName());
        }

        orderItem.setOrderTime(LocalDateTime.now());
        table.getOrderItems().add(orderItem);
        table.setTotalAmount(table.getTotalAmount() + (orderItem.getPrice() * orderItem.getQuantity()));
        return tableRepository.save(table);
    }

    public void settleBill(int tableNumber) {
        Optional<Table> tableOpt = tableRepository.findByTableNumber(tableNumber);
        if (tableOpt.isPresent()) {
            Table table = tableOpt.get();

            double profit = table.getOrderItems().stream()
                    .mapToDouble(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                    .sum();
            tableRepository.save(table);

            clearTable(tableNumber);

            System.out.println("Today's profit from table " + tableNumber + ": " + profit);
        } else {
            throw new IllegalArgumentException("Table not found: " + tableNumber);
        }
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
            System.out.println("Clearing table: " + tableNumber);
            System.out.println("Current order items: " + table.getOrderItems());
            table.getOrderItems().clear();
            table.setTotalAmount(0);
            table.setDiscount(0);
            System.out.println("Table cleared successfully: " + tableNumber);
        } else {
            throw new IllegalArgumentException("Table not found: " + tableNumber);
        }
    }

    public void deleteOrderItem(int tableNumber, String menuItemName) {
        Optional<Table> tableOpt = tableRepository.findByTableNumber(tableNumber);
        if (tableOpt.isPresent()) {
            Table table = tableOpt.get();
            table.getOrderItems().removeIf(orderItem -> orderItem.getMenuItemName().equalsIgnoreCase(menuItemName));
            System.out.println("Deleted order item: " + menuItemName + " from table: " + tableNumber);
        } else {
            throw new IllegalArgumentException("Table not found: " + tableNumber);
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