package com.example.appointfront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppointFrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppointFrontApplication.class, args);
    }
}

// todo: In docView right panel to be exchangeable - doctor form or appointment form.
// todo: In the end - constrain access modifiers, not all f.s need to be public.
// some additional sample data (docs and patients) were in StartingView class, refer to git history if necessary