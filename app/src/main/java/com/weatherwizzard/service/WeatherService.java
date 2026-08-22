package com.weatherwizzard.service;

import com.weatherwizzard.api.WeatherApiClient;
import com.weatherwizzard.api.WeatherParser;
import com.weatherwizzard.model.WeatherMetrics;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Orchestrates {@link WeatherApiClient} and {@link WeatherParser} to produce
 * a complete forecast map for a list of cities.
 *
 * <p>Uses {@link LinkedHashMap} to preserve the insertion order of cities.
 * Returns data directly — no mutable internal state.
 */
public class WeatherService {

    private final WeatherApiClient apiClient;
    private final WeatherParser parser;

    public WeatherService(WeatherApiClient apiClient, WeatherParser parser) {
        this.apiClient = apiClient;
        this.parser = parser;
    }

    /**
     * Fetches and parses the next-day forecast for each city.
     * Cities that fail (network error, bad status, etc.) are skipped with an error message to stderr.
     *
     * @return ordered map of city name → {@link WeatherMetrics}
     */
    public Map<String, WeatherMetrics> fetchForecast(String apiKey, String[] cities) {
        Map<String, WeatherMetrics> result = new LinkedHashMap<>();

        for (String city : cities) {
            try {
                String json = apiClient.fetchRawForecast(city, apiKey);
                result.put(city, parser.parse(json));
            } catch (URISyntaxException e) {
                System.err.println("Invalid URI for city '" + city + "': " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Network error for city '" + city + "': " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Request interrupted for city '" + city + "'.");
            }
        }

        return result;
    }
}
