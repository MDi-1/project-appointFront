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

    public enum Position {  Specialist, Manager, Board  }

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("position")
    private Doctor.Position position;

    @JsonProperty("go_calendar_sync")
    private boolean goCalendarSync;

    @JsonProperty("timeframeIds")
    private List<Long> timeframeIds;

    @JsonProperty("appointmentIds")
    private List<Long> appointmentIds;

    @JsonProperty("medServiceIds")
    private List<Long> medServiceIds;

    @Override
    public String toString() {
        return name + " " + lastName;
    }
}
