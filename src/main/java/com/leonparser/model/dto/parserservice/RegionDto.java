package com.leonparser.model.dto.parserservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionDto {
    private String name;
    private List<LeagueDto> leagues;
}
