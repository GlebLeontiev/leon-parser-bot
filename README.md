# Leon Parser

A Java application that fetches and displays sports betting data from Leon betting platform API. The program retrieves top league matches and betting markets for Football, Tennis, Ice Hockey, and Basketball.

## Main Class

**`com.leonparser.Main`**
- Entry point of the application
- Creates a thread pool with 3 threads
- Initializes all service components
- Start the data fetching and printing process

## Key Classes

### Service Layer

**`LeonDataFetchService`**
- Fetches raw JSON data from Leon API endpoints
- Handles HTTP requests using Jsoup library
- Returns data asynchronously using CompletableFuture

**`LeonParserService`**
- Parses JSON responses into Java DTOs
- Filters sports to only allowed types (Football, Tennis, Ice Hockey, Basketball)
- Extracts top leagues and match information

**`LeonDataProcessingService`**
- Main orchestrator that coordinates data flow
- Processes leagues and matches asynchronously
- Retrieves 2 matches per league (configurable)
- Maintains proper async execution without blocking

**`LeonPrintService`**
- Formats and prints match data to console
- Displays: Sport, League, Match details, Markets, and Odds
- Output format: `Name, Coefficient, ID`

### Supporting Classes

**`MatchDataMapper`**
- Maps raw API DTOs to print-friendly format
- Organizes markets and outcomes for display

**`AppConfig`**
- Loads API URLs from `application.properties`
- Provides configuration to services

## Configuration

Edit `src/main/resources/application.properties` to change API endpoints.
