package com.example.appointfront.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Doctor {

    public enum Position {  Specialist, Administrator, Manager, Board  }

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("position")
    private Doctor.Position position;

    @JsonProperty("timeframeIds")
    private List<Long> timeframeIds;

    @JsonProperty("appointmentIds")
    private List<Long> appointmentIds;

    @JsonProperty("medServiceIds")
    private List<Long> medServiceIds;

    public Doctor(String name, String lastName, Position position) {
        this.name = name;
        this.lastName = lastName;
        this.position = position;
    }

    public String toString1() {
        return "Doctor:(" + "id=" + id + ", name='" + name + '\'' + ", lastName='" + lastName + '\'' +
                ", position=" + position + ", timeframeId=" + timeframeIds +
                ", appointmentIds=" + appointmentIds + ", medServiceIds=" + medServiceIds + ')';
    }

    @Override
    public String toString() {
        return name + " " + lastName;
    }
}
