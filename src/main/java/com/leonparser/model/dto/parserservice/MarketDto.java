package com.leonparser.model.dto.parserservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketDto {
    private String name;
    private List<RunnerDto> runners;
}
