package com.leonparser.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeagueDataDto {
    private Long id;
    private String name;
    private SportDataDto sport;
    private RegionDataDto region;
}
