package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.example.appointfront.data.Patient;
import com.example.appointfront.data.TableEntry;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
public enum Setup {

    SINGLETON_INSTANCE;

    @Setter
    private int admission;
    @Setter
    private boolean timetableLock;
    @Setter
    private Doctor doctor;
    @Setter
    private Patient patient;
    @Setter
    private TableEntry entry;
    @Setter
    private TableEntry entryProcessed;
    @Setter
    private LocalDate targetDay;
    @Setter
    private List<Doctor> doctors;
    @Setter
    private List<Patient> patients;
    @Setter
    private List<MedicalService> msList;
    private final LocalDate startingDay = LocalDate.now();
    static final int COMPANY_STARTING_HOUR = 6;
    static final int WORKDAY_HOURS_AMOUNT  = 12;
}