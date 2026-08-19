
package com.weatherwizzard;

import com.weatherwizzard.config.AppConfig;
import com.weatherwizzard.WeatherHandler;

public class App {
    public void runPipeline(){
        AppConfig config = new AppConfig();

        WeatherHandler handler = new WeatherHandler();

        String[] cities = {"Kyiv", "Amsterdam", "Chisinau", "Madrid"};

        handler.fetchData(config.getApiKey(), cities);

        System.out.println(handler.getWeatherMap());
    }

    public static void main(String[] args) {
        new App().runPipeline();
    }
}
