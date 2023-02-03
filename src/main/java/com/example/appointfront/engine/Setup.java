package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.example.appointfront.data.Patient;
import com.example.appointfront.data.TableEntry;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class Setup {

    private int admission;
    private boolean timetableLock;
    private Doctor doctor;
    private Patient patient;
    private TableEntry entry;
    private TableEntry entryProcessed;
    private LocalDate targetDay;
    private LocalDate startingDay = LocalDate.of(2022, 9, 15); // temporary value for target day in createTables
    private List<Doctor> doctors;
    private List<Patient> patients;
    private List<MedicalService> medicalServices;
}
