package com.example.appointfront.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
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
    private int doctorId;

    public TimeFrame(String date, String timeStart, String timeEnd, int doctorId) {
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.doctorId = doctorId;
    }
}