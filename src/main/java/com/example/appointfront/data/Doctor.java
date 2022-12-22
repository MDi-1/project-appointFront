package com.example.appointfront.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Doctor {

    public enum Position {  Specialist, Administrator, Manager, Board  }

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("position")
    private Doctor.Position position;

    @JsonProperty("timeframeId")
    private Long timeframeId;

    @JsonProperty("appointmentIds")
    private List<Long> appointmentIds;

    @JsonProperty("medServiceIds")
    private List<Long> medServiceIds;

    public Doctor(String name, String lastName, Position position) {
        this.name = name;
        this.lastName = lastName;
        this.position = position;
    }
}
