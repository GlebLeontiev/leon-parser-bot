package com.leonparser.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import com.leonparser.model.dto.parserservice.MarketDto;
import com.leonparser.util.UnixTimeConverter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchDataDto {
    private Long id;
    private String name;
    @JsonDeserialize(using = UnixTimeConverter.class)
    private LocalDateTime kickoff;
    private LeagueDataDto league;
    private List<MarketDto> markets;
}
