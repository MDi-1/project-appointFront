package com.example.appointfront.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Appointment {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("startDateTime")
    private String startDateTime;

    @JsonProperty("price")
    private long price;

    @JsonProperty("doctorId")
    private int doctorId;

    @JsonProperty("patientId")
    private int patientId;

    public Appointment(String startDateTime, int doctorId, int patientId) {
        this.startDateTime = startDateTime;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }
}
