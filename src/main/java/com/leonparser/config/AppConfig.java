package com.leonparser.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private final String sportsLeaguesUrl;
    private final String matchesByLeagueIdUrl;
    private final String marketsByMatchIdUrl;

    private AppConfig(String sportsLeaguesUrl, String matchesByLeagueIdUrl, String marketsByMatchIdUrl) {
        this.sportsLeaguesUrl = sportsLeaguesUrl;
        this.matchesByLeagueIdUrl = matchesByLeagueIdUrl;
        this.marketsByMatchIdUrl = marketsByMatchIdUrl;
    }

    public static AppConfig loadFromProperties() {
        Properties props = new Properties();
        try (InputStream is = AppConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (is == null) {
                throw new RuntimeException("Cannot find application.properties file");
            }
            props.load(is);
            return new AppConfig(
                    props.getProperty("leon.url.sportsLeagues"),
                    props.getProperty("leon.url.matchByLeagueId"),
                    props.getProperty("leon.url.marketByMatchId")
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application properties", e);
        }
    }

    public String getSportsLeaguesUrl() {
        return sportsLeaguesUrl;
    }

    public String getMatchesByLeagueIdUrl() {
        return matchesByLeagueIdUrl;
    }

    public String getMarketsByMatchIdUrl() {
        return marketsByMatchIdUrl;
    }
}
