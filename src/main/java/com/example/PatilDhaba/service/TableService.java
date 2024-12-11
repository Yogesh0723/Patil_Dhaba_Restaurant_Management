package com.example.PatilDhaba.service;

import com.example.PatilDhaba.model.MenuItem;
import com.example.PatilDhaba.model.OrderItem;
import com.example.PatilDhaba.model.Table;
import com.example.PatilDhaba.repository.MenuItemRepository;
import com.example.PatilDhaba.repository.TableRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing table operations in the restaurant.
 * <p>
 * This class provides methods for creating tables, adding order items, applying discounts,
 * settling bills, and retrieving table information.
 * </p>
 */
@Service
public class TableService {

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    /**
     * Initializes the service by creating default tables.
     * This method is called after the bean's properties have been set.
     */
    @PostConstruct
    public void init() {
        for (int i = 1; i <= 30; i++) {
            createTable(i);
        }
    }

    /**
     * Creates a new table or retrieves an existing one.
     *
     * @param tableNumber the number of the table to create
     * @return the created or existing table
     */
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

    /**
     * Adds an order item to a specific table.
     *
     * @param tableNumber the number of the table
     * @param orderItem   the order item to add
     * @return the updated table
     * @throws IllegalArgumentException if the menu item is not found
     */
    public Table addOrderItem(int tableNumber, OrderItem orderItem) {
        Optional<Table> tableOpt = tableRepository.findByTableNumber(tableNumber);
        Table table;
        if (tableOpt.isPresent()) {
            table = tableOpt.get();
        } else {
            table = new Table();
            table.setTableNumber(tableNumber);
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

    /**
     * Applies a discount to a specific table.
     *
     * @param tableNumber the number of the table
     * @param discount    the discount amount to apply
     * @return the updated table
     */
    public Table applyDiscount(int tableNumber, double discount) {
        Optional<Table> tableOpt = tableRepository.findByTableNumber(tableNumber);
        if (!tableOpt.isPresent()) {
            throw new IllegalArgumentException("Table not found");
        }

        Table table = tableOpt.get();
        table.setDiscount(discount);
        return tableRepository.save(table);
    }

    /**
     * Settles the bill for a specific table.
     *
     * @param tableNumber the number of the table to settle the bill for
     * @throws IllegalArgumentException if the table is not found
     */
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

    /**
     * Clears all order items from a specific table.
     *
     * @param tableNumber the number of the table to clear
     * @throws IllegalArgumentException if the table is not found
     */
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

    /**
     * Retrieves a specific table by its number.
     *
     * @param tableNumber the number of the table to retrieve
     * @return the table if found, or null if not found
     */
    public Table getTable(int tableNumber) {
        return tableRepository.findByTableNumber(tableNumber).orElse(null);
    }

    /**
     * Retrieves today's profit from all tables.
     *
     * @return the total profit for today
     */
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