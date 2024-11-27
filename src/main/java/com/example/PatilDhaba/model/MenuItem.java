package com.example.PatilDhaba.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "menu_items")
public class MenuItem {
    @Id
    private String id;
        private String name;
        private double price;
        private String description;
}