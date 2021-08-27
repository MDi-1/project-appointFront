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

    @JsonProperty("id")
    private Long id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("position")
    private String position;

    @JsonProperty("timeframeId")
    private Long timeframeId;

    @JsonProperty("appointmentIds")
    private List<Long> appointmentIds;

    @JsonProperty("medServiceIds")
    private List<Long> medServiceIds;
}
