package com.weatherwizzard;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class WeatherHandler {
    private final Map<String, Map<String, Object>> cityWeatherMap;

    public WeatherHandler() {
        this.cityWeatherMap = new HashMap<>();
    }

    public Map<String, Map<String, Object>> getWeatherMap() {
        return this.cityWeatherMap;
    }

    public static URI createUri(String city, String apiKey) throws URISyntaxException {
        String scheme = "http";
        String host = "api.weatherapi.com";
        String path = "/v1/forecast.json";
        String query = "key=" + apiKey + "&q=" + city + "&days=2";

        return new URI(scheme, host, path, query, null);
    }

    public void writeData(String city, String jsonResponse) {
        JSONObject jsonNode = new JSONObject(jsonResponse);
        JSONArray forecastDays = jsonNode.getJSONObject("forecast").getJSONArray("forecastday");

        JSONObject tomorrow = forecastDays.getJSONObject(1);
        JSONObject dayData = tomorrow.getJSONObject("day");

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("mintemp_c", dayData.getDouble("mintemp_c"));
        metrics.put("maxtemp_c", dayData.getDouble("maxtemp_c"));
        metrics.put("avghumidity", dayData.getInt("avghumidity"));
        metrics.put("maxwind_kph", dayData.getDouble("maxwind_kph"));
        
        String windDir = tomorrow.getJSONArray("hour").getJSONObject(12).getString("wind_dir");
        metrics.put("wind_dir", windDir);

        cityWeatherMap.put(city, metrics);
    }

    public void fetchData(String apiKey, String[] cities) {
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("API key is missing!");
            return;
        }

        try {
            HttpClient client = HttpClient.newHttpClient();
            for (String city: cities){
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(createUri(city, apiKey))
                        .header("Accept", "application/json")
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    writeData(city, response.body());
                } else {
                    System.err.println("Failed to fetch weather data. Status code: " + response.statusCode());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}