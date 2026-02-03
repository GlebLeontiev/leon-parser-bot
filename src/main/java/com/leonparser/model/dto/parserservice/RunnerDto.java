package com.leonparser.model.dto.parserservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RunnerDto {
    private Long id;
    private String name;
    private Double price;
}
