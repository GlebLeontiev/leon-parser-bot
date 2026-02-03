package com.leonparser.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonparser.exception.ProcessingJsonException;
import com.leonparser.model.dto.MatchDataDto;
import com.leonparser.model.dto.parserservice.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

public class LeonParserService {

    private static final String JSON_PATH_EVENTS = "events";
    private static final List<String> ALLOWED_SPORTS = List.of(
            "Football", "Tennis", "Ice Hockey", "Basketball"
    );

    private final ObjectMapper objectMapper;
    private final LeonDataFetchService fetcherService;

    public LeonParserService(ObjectMapper objectMapper, LeonDataFetchService fetcherService) {
        this.objectMapper = objectMapper;
        this.fetcherService = fetcherService;
    }

    public List<Long> getAllSportsTopLeagueIds() {
        try {
            String allSportsLeagues = fetcherService.getAllSportsLeagues();
            List<SportDto> sportDtos = objectMapper.readValue(allSportsLeagues, new TypeReference<>() {});

            return ALLOWED_SPORTS.stream()
                    .flatMap(allowedSport -> sportDtos.stream()
                            .filter(sport -> allowedSport.equalsIgnoreCase(sport.getName()))
                            .limit(1))
                    .flatMap(sport -> sport.getRegions().stream()
                            .flatMap(region -> region.getLeagues().stream())
                            .filter(LeagueDto::isTopLeague))
                    .map(LeagueDto::getId)
                    .collect(toList());

        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve sports and leagues", e);
        }
    }

    public CompletableFuture<List<MatchDto>> getMatchesByLeagueId(Long leagueId, Integer amountOfMatches) {
        try {
            return fetcherService.getMatchesByLeagueId(leagueId)
                    .thenApply(data -> readJsonTree(data, JSON_PATH_EVENTS))
                    .thenApply(
                            matches -> matches.valueStream()
                                    .map(this::parseMatch)
                                    .limit(amountOfMatches)
                                    .collect(toList())
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<MatchDataDto> getMarketsByMatchId(Long matchId) {
        return fetcherService
                .getMarketsByMatchId(matchId)
                .thenApply(this::readValueToPrintMatchDataDto);
    }

    private MatchDataDto readValueToPrintMatchDataDto(String marketsByMatchId) {
        try {
            return objectMapper.readValue(marketsByMatchId, MatchDataDto.class);
        } catch (Exception e) {
            throw new ProcessingJsonException("Could not parse Json string to MatchDataDto class", e);
        }
    }

    private JsonNode readJsonTree(String data, String pathToRead) {
        try {
            return objectMapper.readTree(data).path(pathToRead);
        } catch (Exception e) {
            throw new ProcessingJsonException("Could not retrieve Json string to JsonNode", e);
        }
    }

    private MatchDto parseMatch(JsonNode matches) {
        try {
            return objectMapper.treeToValue(matches, MatchDto.class);
        } catch (JsonProcessingException e) {
            throw new ProcessingJsonException("Could not parse JsonNode to MatchDto class", e);
        }
    }
}
