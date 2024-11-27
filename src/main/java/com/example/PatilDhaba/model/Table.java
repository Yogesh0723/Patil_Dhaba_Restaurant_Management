package com.example.PatilDhaba.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "tables")
@Data
public class Table {
    @Id
    private String id;
    private int tableNumber;
    private List<OrderItem> orderItems = new ArrayList<>();
    private double totalAmount;
    private double discount;
    private int kotNumber;

    public double getFinalAmount() {
        return totalAmount - discount;
    }
}