package com.example.PatilDhaba.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Document(collection = "employees")
public class Employee {
    @Id
    private String id;

    private String name;
    private int age;
    private String contactNumber;
    private String address;
    private String photo;
    private LocalDate dateOfJoining;
    private double salaryPerDay;
    private double salaryPerMonth;

    @Field("attendance")
    private Map<Integer, List<Double>> attendance = new HashMap<>();

    // Ensure attendance is always a map of month and list of Double
    public void setAttendance(Map<Integer, List<? extends Number>> attendance) {
        this.attendance = attendance.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(Number::doubleValue)
                                .toList()));
    }
}