package com.leonparser.model.dto.parserservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeagueDto {
    private Long id;
    private String name;
    private Boolean top;

    public Boolean isTopLeague() {
        return top;
    }
}
