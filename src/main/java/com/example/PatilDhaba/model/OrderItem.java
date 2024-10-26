package com.example.PatilDhaba.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderItem {
    private String menuItemName;
    private int quantity;
    private double price;
    private LocalDateTime orderTime;
}