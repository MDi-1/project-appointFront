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
    private Long id;

    @JsonProperty("firstName")
    private String firstName;

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

    public Doctor(String firstName, String lastName, Position position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(id, doctor.id) && Objects.equals(firstName, doctor.firstName)
                && Objects.equals(lastName, doctor.lastName) && position == doctor.position
                && Objects.equals(timeframeId, doctor.timeframeId)
                && Objects.equals(appointmentIds, doctor.appointmentIds)
                && Objects.equals(medServiceIds, doctor.medServiceIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, position, timeframeId, appointmentIds, medServiceIds);
    }
}
