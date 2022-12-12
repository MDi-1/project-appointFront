package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.Patient;
import com.example.appointfront.data.TableEntry;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Data
@Component
public class Setup {

    private boolean admission;
    private boolean timetableLock;
    private Doctor doctor;
    private Patient patient;
    private TableEntry entry;
    private TableEntry entryProcessed;
    private LocalDate targetDay;
    private LocalDate startingDay = LocalDate.of(2022, 9, 15); // temporary value for target day in createTables
}
