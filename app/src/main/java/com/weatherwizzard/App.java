
package com.weatherwizzard;

import com.weatherwizzard.config.AppConfig;
import com.weatherwizzard.WeatherHandler;

public class App {
    public void runPipeline(){
        AppConfig config = new AppConfig();

        WeatherHandler handler = new WeatherHandler();

        handler.fetchData(config.getApiKey(), "Kyiv");
    }

    public static void main(String[] args) {
        new App().runPipeline();
    }
}
