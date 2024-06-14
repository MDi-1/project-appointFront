package com.example.appointfront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;

@SpringBootApplication
public class AppointFrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppointFrontApplication.class, args);
        System.out.println(
                "SpringVersion= " + SpringVersion.getVersion() +
                "\nSystem.getProperty(\"java.version\")= " + System.getProperty("java.version")
        );
    }
}