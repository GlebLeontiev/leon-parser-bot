package com.leonparser.service.impl;

import com.leonparser.mapper.MatchDataMapper;
import com.leonparser.model.dto.PrintMatchDataDto;
import com.leonparser.model.dto.parserservice.MatchDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class LeonDataProcessingService {

    private static final Integer AMOUNT_OF_MATCHES = 2;

    private final LeonParserService parserService;
    private final LeonPrintService printService;
    private final MatchDataMapper matchDataMapper;
    private final ExecutorService executorService;

    public LeonDataProcessingService(
            LeonParserService parserService,
            LeonPrintService printService,
            MatchDataMapper matchDataMapper,
            ExecutorService executorService) {
        this.parserService = parserService;
        this.printService = printService;
        this.matchDataMapper = matchDataMapper;
        this.executorService = executorService;
    }

    public void printTopLeaguesMatchesMarketsDetails() {
        List<Long> allSportsTopLeagueIds = parserService.getAllSportsTopLeagueIds();
        List<CompletableFuture<Void>> leagueFutureList = new ArrayList<>();

        // Process each league
        for (Long leagueId : allSportsTopLeagueIds) {
            CompletableFuture<Void> leagueFuture = parserService.getMatchesByLeagueId(leagueId, AMOUNT_OF_MATCHES)
                    .thenComposeAsync(this::processMatchesAsync, executorService);

            leagueFutureList.add(leagueFuture);
        }

        // Wait for all leagues to complete
        CompletableFuture<Void> allLeagues = CompletableFuture.allOf(
                leagueFutureList.toArray(new CompletableFuture[0])
        );
        allLeagues.join();
    }

    private CompletableFuture<Void> processMatchesAsync(List<MatchDto> matches) {
        List<CompletableFuture<Void>> matchFutureList = new ArrayList<>();

        // Process each match
        for (MatchDto match : matches) {
            Long matchId = match.getId();

            CompletableFuture<Void> matchFuture = parserService
                    .getMarketsByMatchId(matchId)
                    .thenApply(this::mapMatchData)
                    .thenAccept(this::printMatchData);

            matchFutureList.add(matchFuture);
        }

        return CompletableFuture.allOf(matchFutureList.toArray(new CompletableFuture[0]));
    }

    private PrintMatchDataDto mapMatchData(com.leonparser.model.dto.MatchDataDto marketData) {
        return matchDataMapper.map(marketData);
    }

    private void printMatchData(PrintMatchDataDto printData) {
        printService.print(printData);
    }
}
