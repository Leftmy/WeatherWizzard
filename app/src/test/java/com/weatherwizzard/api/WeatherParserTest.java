package com.weatherwizzard.api;

import com.weatherwizzard.model.WeatherMetrics;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link WeatherParser}.
 * Uses a hardcoded inline JSON string — no network calls, no I/O.
 */
public class WeatherParserTest {

    /**
     * Minimal valid WeatherAPI.com forecast response:
     * - index 0: today (ignored)
     * - index 1: tomorrow (parsed)
     * Hours: "NW" x2, "N" x1 → dominant = "NW"
     */
    private static final String SAMPLE_JSON =
            "{\n" +
            "  \"forecast\": {\n" +
            "    \"forecastday\": [\n" +
            "      {\n" +
            "        \"date\": \"2026-08-22\",\n" +
            "        \"day\": {},\n" +
            "        \"hour\": []\n" +
            "      },\n" +
            "      {\n" +
            "        \"date\": \"2026-08-23\",\n" +
            "        \"day\": {\n" +
            "          \"mintemp_c\": 15.5,\n" +
            "          \"maxtemp_c\": 28.3,\n" +
            "          \"avghumidity\": 65,\n" +
            "          \"maxwind_kph\": 22.5\n" +
            "        },\n" +
            "        \"hour\": [\n" +
            "          { \"wind_dir\": \"NW\" },\n" +
            "          { \"wind_dir\": \"NW\" },\n" +
            "          { \"wind_dir\": \"N\"  }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    private final WeatherParser parser = new WeatherParser();

    @Test
    public void parse_extractsDate() {
        WeatherMetrics result = parser.parse(SAMPLE_JSON);
        assertEquals("2026-08-23", result.date());
    }

    @Test
    public void parse_extractsMinTemp() {
        WeatherMetrics result = parser.parse(SAMPLE_JSON);
        assertEquals(15.5, result.minTempC(), 0.001);
    }

    @Test
    public void parse_extractsMaxTemp() {
        WeatherMetrics result = parser.parse(SAMPLE_JSON);
        assertEquals(28.3, result.maxTempC(), 0.001);
    }

    @Test
    public void parse_extractsHumidity() {
        WeatherMetrics result = parser.parse(SAMPLE_JSON);
        assertEquals(65, result.avgHumidity());
    }

    @Test
    public void parse_extractsMaxWindKph() {
        WeatherMetrics result = parser.parse(SAMPLE_JSON);
        assertEquals(22.5, result.maxWindKph(), 0.001);
    }

    @Test
    public void parse_extractsDominantWindDir() {
        // "NW" appears twice, "N" once — dominant should be "NW"
        WeatherMetrics result = parser.parse(SAMPLE_JSON);
        assertEquals("NW", result.windDir());
    }
}
