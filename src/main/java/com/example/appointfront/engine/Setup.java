package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.example.appointfront.data.Patient;
import com.example.appointfront.data.TableEntry;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Setup {

    SINGLETON_INSTANCE;

    @Setter
    private int admission;
    @Setter
    private Doctor doctor;
    @Setter
    private Patient patient;
    @Setter
    private TableEntry entry;
    @Setter
    private LocalDate targetDay;
    @Setter
    private List<Doctor> doctors;
    @Setter
    private List<Patient> patients;
    @Getter @Setter
    static List<MedicalService> msList;
    static final LocalDate STARTING_DAY = LocalDate.now();
    static final int EARLIEST_STARTING_HOUR = 6;
    static final int WORKDAY_HOURS_AMOUNT  = 12;

    public static List<MedicalService> getCurrentDoctorMsList() {
        return Setup.SINGLETON_INSTANCE.getDoctor().getMedServiceIds().stream()
                .map(e -> msList.stream()
                        .filter(ms -> ms.getId().equals(e)).findFirst().orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList());
    }
}