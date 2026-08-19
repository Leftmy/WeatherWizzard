
package com.weatherwizzard;

import com.weatherwizzard.config.AppConfig;
import com.weatherwizzard.WeatherHandler;
import java.util.Map;

public class App {
    public Map<String, Map<String, Object>> runPipeline(){
        AppConfig config = new AppConfig();

        WeatherHandler handler = new WeatherHandler();

        String[] cities = {"Kyiv", "Amsterdam", "Chisinau", "Madrid"};

        handler.fetchData(config.getApiKey(), cities);

        return handler.getWeatherMap();
    }

    public void printTable(Map<String, Map<String, Object>> weatherData) {
        if (weatherData == null || weatherData.isEmpty()) {
            System.out.println("No weather data available.");
            return;
        }

        Map<String, Object> firstCityMetrics = weatherData.values().iterator().next();
        Object dateObj = firstCityMetrics != null ? firstCityMetrics.get("date") : null;
        String forecastDate = (dateObj != null) ? dateObj.toString() : "N/A";

        System.out.printf("| %-12s | %-65s |%n", "City", forecastDate + " (Forecast)");
        System.out.println("-".repeat(84));

        for (Map.Entry<String, Map<String, Object>> entry : weatherData.entrySet()) {
            String city = entry.getKey();
            Map<String, Object> m = entry.getValue();

            if (m == null) continue;

            double minTemp = m.get("mintemp_c") instanceof Number ? ((Number) m.get("mintemp_c")).doubleValue() : 0.0;
            double maxTemp = m.get("maxtemp_c") instanceof Number ? ((Number) m.get("maxtemp_c")).doubleValue() : 0.0;
            int humidity = m.get("avghumidity") instanceof Number ? ((Number) m.get("avghumidity")).intValue() : 0;
            double maxWind = m.get("maxwind_kph") instanceof Number ? ((Number) m.get("maxwind_kph")).doubleValue() : 0.0;
            String windDir = m.get("wind_dir") != null ? m.get("wind_dir").toString() : "N/A";

            String formattedMetrics = String.format(
                "Min: %.1f deg C | Max: %.1f deg C | Hum: %d%% | Wind: %.1f kph %s",
                minTemp, maxTemp, humidity, maxWind, windDir
            );

            System.out.printf("| %-12s | %-65s |%n", city, formattedMetrics);
        }
    }

    public static void main(String[] args) {
        App app = new App();
        app.printTable(app.runPipeline());
    }

}
