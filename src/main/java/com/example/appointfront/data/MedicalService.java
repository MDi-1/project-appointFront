package com.example.appointfront.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicalService {

    public enum ServiceName { Physician, Dermatologist, Gynecologist, Gastroenterologist, Laryngologist, Orthopedist }

    @JsonProperty("id")
    private Long id;

    @JsonProperty("serviceName")
    private MedicalService.ServiceName serviceName;

    @JsonProperty("description")
    private String description;
}
