package com.example.appointfront.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@AllArgsConstructor
@Data
public class TableEntry {
    private String weekday;
    private String status;
    private LocalTime time;
    private Long duration;
    private Patient patient;
    private Doctor doctor;
}
