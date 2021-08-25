package com.example.appointfront;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Dto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @Override
    public String toString() {
        return "Dto { " + "id= " + id + ", name= '" + name + '\'' + '}';
    }
}
