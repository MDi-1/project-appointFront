package com.example.appointfront.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeFrame {

    public enum TfStatus {  Present, Day_Off  }

    @JsonProperty("id")
    private Long id;

    @JsonProperty("timeFrameDate")
    private String date;

    @JsonProperty("timeStart")
    private String timeStart;

    @JsonProperty("timeEnd")
    private String timeEnd;

    @JsonProperty("status")
    private TimeFrame.TfStatus status;

    @JsonProperty("doctorId")
    private Long doctorId;

    public TimeFrame(String date, String timeStart, String timeEnd, TfStatus status, Long doctorId) {
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.status = status;
        this.doctorId = doctorId;
    }
}