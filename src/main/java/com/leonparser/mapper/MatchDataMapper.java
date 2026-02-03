package com.leonparser.mapper;

import com.leonparser.model.dto.MatchDataDto;
import com.leonparser.model.dto.PrintMatchDataDto;
import com.leonparser.model.dto.parserservice.MarketDto;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class MatchDataMapper {

    public PrintMatchDataDto map(MatchDataDto dto) {
        PrintMatchDataDto printDto = new PrintMatchDataDto();

        printDto.setId(dto.getId());
        printDto.setName(dto.getName());
        printDto.setKickoff(dto.getKickoff());
        printDto.setLeague(dto.getLeague());
        printDto.setMarkets(dto.getMarkets().stream().collect(groupingBy(
                MarketDto::getName,
                LinkedHashMap::new,
                Collectors.flatMapping(
                        marketDto -> marketDto.getRunners().stream(),
                        Collectors.toList()
                )
        )));

        return printDto;
    }
}
