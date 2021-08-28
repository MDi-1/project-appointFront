package com.example.appointfront.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Appointment {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("startDate")
    private LocalDateTime startDate;

    @JsonProperty("duration")
    private long duration;

    @JsonProperty("doctorId")
    private Long doctorId;

    @JsonProperty("patientId")
    private Long patientId;
}
