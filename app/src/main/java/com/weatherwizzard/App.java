package com.weatherwizzard;

import com.weatherwizzard.api.WeatherApiClient;
import com.weatherwizzard.api.WeatherParser;
import com.weatherwizzard.config.AppConfig;
import com.weatherwizzard.model.WeatherMetrics;
import com.weatherwizzard.service.WeatherService;

import java.util.Map;

public class App {

    private static final String[] CITIES = {"Kyiv", "Amsterdam", "Chisinau", "Madrid"};

    public static void main(String[] args) {
        AppConfig config = new AppConfig();
        WeatherService service = new WeatherService(new WeatherApiClient(), new WeatherParser());

        Map<String, WeatherMetrics> forecast = service.fetchForecast(config.getApiKey(), CITIES);
        printTable(forecast);
    }

    static void printTable(Map<String, WeatherMetrics> weatherData) {
        if (weatherData == null || weatherData.isEmpty()) {
            System.out.println("No weather data available.");
            return;
        }

        String forecastDate = weatherData.values().iterator().next().date();
        String separator = "-".repeat(84);

        System.out.println(separator);
        System.out.printf("| %-12s | %-65s |%n", "City", forecastDate + " (Forecast)");
        System.out.println(separator);

        for (Map.Entry<String, WeatherMetrics> entry : weatherData.entrySet()) {
            WeatherMetrics m = entry.getValue();
            String row = String.format(
                    "Min: %.1f deg C | Max: %.1f deg C | Hum: %d%% | Wind: %.1f kph %s",
                    m.minTempC(), m.maxTempC(), m.avgHumidity(), m.maxWindKph(), m.windDir()
            );
            System.out.printf("| %-12s | %-65s |%n", entry.getKey(), row);
        }

        System.out.println(separator);
    }
}
