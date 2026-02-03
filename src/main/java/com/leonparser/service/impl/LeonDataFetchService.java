package com.leonparser.service.impl;

import org.jsoup.Jsoup;
import com.leonparser.exception.ApiCallException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class LeonDataFetchService {
    private final String urlGetAllSportsLeagues;
    private final String urlGetMatchesByLeagueId;
    private final String urlGetMarketsByMatchId;
    private final ExecutorService executorService;

    public LeonDataFetchService(
            String urlGetAllSportsLeagues,
            String urlGetMatchesByLeagueId,
            String urlGetMarketsByMatchId,
            ExecutorService executorService) {
        this.urlGetAllSportsLeagues = urlGetAllSportsLeagues;
        this.urlGetMatchesByLeagueId = urlGetMatchesByLeagueId;
        this.urlGetMarketsByMatchId = urlGetMarketsByMatchId;
        this.executorService = executorService;
    }

    public String getAllSportsLeagues() {
        return fetchData(urlGetAllSportsLeagues);
    }

    public CompletableFuture<String> getMatchesByLeagueId(Long leagueId) {
        String url = urlGetMatchesByLeagueId.formatted(leagueId);
        return fetchDataAsync(url);
    }

    public CompletableFuture<String> getMarketsByMatchId(Long matchId) {
        String url = urlGetMarketsByMatchId.formatted(matchId);
        return fetchDataAsync(url);
    }

    private CompletableFuture<String> fetchDataAsync(String url) {
        return CompletableFuture.supplyAsync(() -> fetchData(url), executorService);
    }

    private String fetchData(String url) {
        try {
            String body = Jsoup.connect(url).ignoreContentType(true).execute().body();
            if (body.isBlank()) {
                throw new ApiCallException("API response returned empty data");
            }
            return body;
        } catch (Exception e) {
            throw new ApiCallException(e);
        }
    }
}
