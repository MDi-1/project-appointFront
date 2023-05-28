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
    private int price;

    @JsonProperty("ownersId")
    private Long doctorId;

    @JsonProperty("patientId")
    private Long patientId;

    public Appointment(String startDateTime, Long doctorId, Long patientId) {
        this.startDateTime = startDateTime;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }
}
