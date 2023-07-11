package com.example.appointfront.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class TableEntry {
    private LocalDateTime entryDateTime;
    private String status;
    private Patient patient;
    private Doctor doctor;
    private Appointment attributedApp;
}
