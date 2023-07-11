package com.example.appointfront.data;

import com.example.appointfront.engine.Setup;
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

    @JsonProperty("medicalServiceId")
    private Long medicalServiceId;

    @JsonProperty("ownersId")
    private Long doctorId;

    @JsonProperty("patientId")
    private Long patientId;

    public Appointment(String startDateTime, Long medicalServiceId, Long doctorId, Long patientId) {
        this.startDateTime = startDateTime;
        this.price = Setup.SINGLETON_INSTANCE.getMsList().stream().filter(ms -> ms.getId().equals(medicalServiceId))
                .map(MedicalService::getPrice).findAny().orElseThrow(IllegalArgumentException::new);
        this.medicalServiceId = medicalServiceId;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }
}
