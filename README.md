# WeatherWizzard

A command-line Java application that fetches next-day weather forecasts for **Kyiv, Amsterdam, Chisinau, and Madrid** using the [WeatherAPI.com](https://www.weatherapi.com/) REST API and prints the results as a formatted table to stdout.

**Example output:**
```
------------------------------------------------------------------------------------
| City         | 2026-08-23 (Forecast)                                             |
------------------------------------------------------------------------------------
| Kyiv         | Min: 17.2 °C | Max: 29.8 °C | Hum: 54% | Wind: 18.4 kph NW      |
| Amsterdam    | Min: 13.0 °C | Max: 21.5 °C | Hum: 72% | Wind: 31.0 kph W       |
| Chisinau     | Min: 18.6 °C | Max: 32.1 °C | Hum: 45% | Wind: 14.7 kph NE      |
| Madrid       | Min: 22.3 °C | Max: 38.4 °C | Hum: 28% | Wind: 11.2 kph SSW     |
------------------------------------------------------------------------------------
```

Each row shows:
- Minimum Temperature (°C)
- Maximum Temperature (°C)
- Average Humidity (%)
- Max Wind Speed (kph)
- Dominant Wind Direction (most frequent across all hourly forecasts)

---

## Technologies Used

| Technology | Purpose |
|---|---|
| **Java 21** | Core language |
| **Gradle 8.10** | Build system and dependency management |
| **`java.net.http.HttpClient`** | Built-in HTTP client (Java 11+) for API requests |
| **`org.json`** | Lightweight JSON parsing |
| **JUnit 4** | Unit testing |

---

## How to Run

### Prerequisites

- JDK 21+
- A free API key from [weatherapi.com](https://www.weatherapi.com/signup.aspx)

### 1. Set up the API key

Create a `.env` file in the project root:

```
WEATHER_API_KEY=your_api_key_here
```

Alternatively, export it as an environment variable (takes priority over `.env`):

```bash
# Linux / macOS
export WEATHER_API_KEY=your_api_key_here

# Windows PowerShell
$env:WEATHER_API_KEY="your_api_key_here"
```

### 2. Run the application

```bash
./gradlew run          # Linux / macOS
.\gradlew.bat run      # Windows
```

### 3. Run the tests

```bash
./gradlew test          # Linux / macOS
.\gradlew.bat test      # Windows
```

All tests are **offline** — no network calls are made during testing. The test suite covers:
- `WeatherApiClientTest` — URI construction (scheme, host, path, query params)
- `WeatherParserTest` — JSON parsing and dominant wind direction logic, using an inline JSON fixture

---

## Project Structure

```
app/src/
├── main/java/com/weatherwizzard/
│   ├── App.java                   # Entry point + table rendering
│   ├── config/AppConfig.java      # API key loading (.env / env var)
│   ├── model/WeatherMetrics.java  # Immutable data record
│   ├── api/
│   │   ├── WeatherApiClient.java  # HTTP layer
│   │   └── WeatherParser.java     # JSON → WeatherMetrics
│   └── service/
│       └── WeatherService.java    # Orchestration
└── test/java/com/weatherwizzard/
    └── api/
        ├── WeatherApiClientTest.java
        └── WeatherParserTest.java
```

---

## Possible Improvements

- **Parallel requests** — city forecasts are currently fetched sequentially. Switching to `HttpClient.sendAsync()` with `CompletableFuture.allOf()` would fetch all 4 cities concurrently.
- **Configurable cities** — cities are currently hardcoded. Accepting them as CLI arguments or via config would make the app more flexible.
- **Structured logging** — replace `System.err.println` with a proper logging framework (SLF4J + Logback) to support log levels and output formatting.
- **Caching** — add a simple file-based or in-memory cache with a TTL to avoid redundant API calls on repeated runs within the same day.