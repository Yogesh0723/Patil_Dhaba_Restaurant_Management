package com.example.PatilDhaba.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private List<Double> attendance = new ArrayList<>();

    // Ensure attendance is always a list of Double
    public void setAttendance(List<? extends Number> attendance) {
        this.attendance = attendance.stream()
                .map(Number::doubleValue)
                .toList();
    }
}