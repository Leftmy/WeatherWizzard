package com.weatherwizzard.config;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;


public class AppConfig{
    private String apiKey;

    public String getApiKey(){
        return this.apiKey;
    }

    public AppConfig(){
        // First, try to read from environment variable (e.g. when deployed in CI/CD)
        String envApiKey = System.getenv("WEATHER_API_KEY");
        if (envApiKey != null && !envApiKey.isBlank()) {
            this.apiKey = envApiKey;
            return;
        }

        // Fallback: read from .env file
        Properties properties = new Properties();

        File envFile = new File(".env");
        if (!envFile.exists()) {
            envFile = new File("../.env");
        }
        
        try (FileInputStream fis = new FileInputStream(envFile)) {
            properties.load(fis);
            this.apiKey = properties.getProperty("WEATHER_API_KEY");
        } catch (IOException e) {
            System.err.println(".env file not found or unreadable: " + e.getMessage());
        }
    }
}