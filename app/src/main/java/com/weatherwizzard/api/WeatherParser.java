package com.weatherwizzard.api;

import com.weatherwizzard.model.WeatherMetrics;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Responsible solely for parsing a raw JSON forecast response into a {@link WeatherMetrics} record.
 * Has no knowledge of HTTP or application state.
 */
public class WeatherParser {

    /**
     * Parses the raw JSON string returned by the WeatherAPI.com forecast endpoint
     * and extracts tomorrow's (index 1) forecast day metrics.
     */
    public WeatherMetrics parse(String jsonResponse) {
        JSONObject json = new JSONObject(jsonResponse);
        JSONObject tomorrow = json
                .getJSONObject("forecast")
                .getJSONArray("forecastday")
                .getJSONObject(1);

        JSONObject day = tomorrow.getJSONObject("day");

        return new WeatherMetrics(
                tomorrow.getString("date"),
                day.getDouble("mintemp_c"),
                day.getDouble("maxtemp_c"),
                day.getInt("avghumidity"),
                day.getDouble("maxwind_kph"),
                dominantWindDir(tomorrow.getJSONArray("hour"))
        );
    }

    /**
     * Returns the most frequently occurring wind direction across all hourly entries.
     * Falls back to "N/A" if the hours array is empty.
     */
    private static String dominantWindDir(JSONArray hours) {
        return IntStream.range(0, hours.length())
                .mapToObj(i -> hours.getJSONObject(i).getString("wind_dir"))
                .collect(Collectors.groupingBy(dir -> dir, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }
}
