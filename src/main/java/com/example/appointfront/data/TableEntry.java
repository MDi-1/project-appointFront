package com.example.appointfront.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Data
public class TableEntry {
    private LocalDate weekday;
    private String status;
    private LocalTime time;
    private Long duration;
    private Patient patient;
    private Doctor doctor;
    private Long attributedApp;
}
