package com.example.appointfront.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    private Integer testId;

    public TestDto(String name) {
        this.name = name;
    }

    public TestDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() { return "TestDto{" +"id=" +id +", name='" +name +'\'' +", testId=" +testId +'}'; }
}
