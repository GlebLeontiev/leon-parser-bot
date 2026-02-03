package com.leonparser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.leonparser.model.dto.parserservice.RunnerDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrintMatchDataDto {
    private Long id;
    private String name;
    private LocalDateTime kickoff;
    private LeagueDataDto league;
    private Map<String, List<RunnerDto>> markets;
}
