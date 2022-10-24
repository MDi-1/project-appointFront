package com.example.appointfront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppointFrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppointFrontApplication.class, args);
    }
}

// todo: probably it'd be good idea to relocate class fields such as current patient (logged user) from BackendClient
// ...class to InitHeader class.
// todo: In docView right panel to be exchangeable - doctor form or appointment form.
// todo: In the end - constrain access modifiers, not all f.s need to be public.
// > as this app is finished and there is enough time please refactor createTables() and buildWeekday in DoctorView...
//...instead of TableEntry objects in the role of wrapping Appointment objects there should be Appointment class...
//...expanded by some class attributes and used in timetable.
// > some additional sample data (docs and patients) were in StartingView class, refer to git history if necessary
// > maybe, some day - refactor BackendClient that whole requests with try-catch blocks are wrapped into single f. which
// ...accepts parameters like Generic and endpoint string.