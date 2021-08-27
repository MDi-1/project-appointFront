package com.example.appointfront.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TestDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @Override
    public String toString() {
        return "TestDto { " + "id= " + id + ", name= '" + name + '\'' + '}';
    }
}
