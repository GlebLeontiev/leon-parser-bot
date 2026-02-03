package com.leonparser.service.impl;

import com.leonparser.model.dto.PrintMatchDataDto;
import com.leonparser.model.dto.parserservice.RunnerDto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class LeonPrintService {

    private static final String HEADER_TEMPLATE = """
            %s, %s %s
            \t%s, %s UTC, %d
            """;
    private static final String MARKET_TEMPLATE = """
            \t\t%s
            """;
    private static final String RUNNER_TEMPLATE = """
            \t\t\t%s, %.2f, %d
            """;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void print(PrintMatchDataDto matchDataDto) {
        StringBuilder stringBuilder = new StringBuilder();

        appendHeader(stringBuilder, matchDataDto);
        appendMarkets(stringBuilder, matchDataDto.getMarkets());

        System.out.println(stringBuilder);
    }

    private void appendHeader(StringBuilder sb, PrintMatchDataDto dto) {
        sb.append(HEADER_TEMPLATE.formatted(
                dto.getLeague().getSport().getName(),
                dto.getLeague().getRegion().getName(),
                dto.getLeague().getName(),
                dto.getName(),
                dto.getKickoff().format(DATE_TIME_FORMATTER),
                dto.getId()
        ));
    }

    private void appendMarkets(StringBuilder sb, Map<String, List<RunnerDto>> markets) {
        for (Map.Entry<String, List<RunnerDto>> market : markets.entrySet()) {
            sb.append(MARKET_TEMPLATE.formatted(market.getKey()));
            for (RunnerDto runner : market.getValue()) {
                sb.append(RUNNER_TEMPLATE.formatted(
                        runner.getName(),
                        runner.getPrice(),
                        runner.getId()
                ));
            }
        }
    }
}
