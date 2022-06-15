package com.example.appointfront.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TimeFrame {
    private Long id;
    private String date;
    private String timeStart;
    private String timeEnd;
    private Long doctorId;
}