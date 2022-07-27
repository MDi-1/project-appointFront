package com.example.appointfront.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TimeFrame {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("timeFrameDate")
    private String date;

    @JsonProperty("timeStart")
    private String timeStart;

    @JsonProperty("timeEnd")
    private String timeEnd;

    @JsonProperty("doctorId")
    private Long doctorId;
}