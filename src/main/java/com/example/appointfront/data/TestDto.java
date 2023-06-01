package com.example.appointfront.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestDto {
    public enum RankLabel { FIRST, SECOND, THIRD, FOURTH }

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("trueOrFalse")
    private boolean trueOrFalse;

    @JsonProperty("rankLabel")
    private RankLabel rankLabel;

    private Integer testId;

    public TestDto(String name) {
        this.name = name;
    }

    public TestDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
