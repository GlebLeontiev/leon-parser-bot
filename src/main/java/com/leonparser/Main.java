package com.leonparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonparser.config.AppConfig;
import com.leonparser.mapper.MatchDataMapper;
import com.leonparser.service.impl.LeonDataFetchService;
import com.leonparser.service.impl.LeonDataProcessingService;
import com.leonparser.service.impl.LeonParserService;
import com.leonparser.service.impl.LeonPrintService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int THREAD_POOL_SIZE = 3;

    public static void main(String[] args) {
        ExecutorService executor = null;
        try {
            AppConfig config = AppConfig.loadFromProperties();

            executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            ObjectMapper objectMapper = new ObjectMapper();

            LeonDataFetchService fetchService = new LeonDataFetchService(
                    config.getSportsLeaguesUrl(),
                    config.getMatchesByLeagueIdUrl(),
                    config.getMarketsByMatchIdUrl(),
                    executor
            );

            LeonParserService parserService = new LeonParserService(
                    objectMapper, fetchService
            );

            LeonPrintService printService = new LeonPrintService();
            MatchDataMapper mapper = new MatchDataMapper();

            LeonDataProcessingService processingService = new LeonDataProcessingService(
                    parserService, printService, mapper, executor
            );

            processingService.printTopLeaguesMatchesMarketsDetails();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            shutdownExecutor(executor);
        }
    }

    private static void shutdownExecutor(ExecutorService executor) {
        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
