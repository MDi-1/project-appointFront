package com.example.appointfront.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeFrame {

    public enum TfStatus { Present, Day_Off, Holiday } // is holiday never used? fixme

    @JsonProperty("id")
    private Long id;

    @JsonProperty("timeFrameDate")
    private String date;

    @JsonProperty("timeStart")
    private String timeStart;

    @JsonProperty("timeEnd")
    private String timeEnd;

    @JsonProperty("status")
    private TimeFrame.TfStatus tfStatus;

    @JsonProperty("ownersId")
    private Long ownersId;

    public TimeFrame(String date, String timeStart, String timeEnd, TfStatus status, Long ownersId) {
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.tfStatus = status;
        this.ownersId = ownersId;
    }
}