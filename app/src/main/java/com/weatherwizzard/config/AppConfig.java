package com.weatherwizzard.config;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;


public class AppConfig{
    private String API_KEY;

    public String getApiKey(){
        return this.API_KEY;
    }

    public AppConfig(){
        Properties properties = new Properties();

        File envFile = new File(".env");
        if (!envFile.exists()) {
            envFile = new File("../.env");
        }
        
        try (FileInputStream fis = new FileInputStream(envFile)) {
            // Load the .env file
            properties.load(fis);
            
            // Fetch values
            String apiKey = properties.getProperty("WEATHER_API_KEY");

            this.API_KEY = apiKey;

            System.out.println("Successfully read API Key!");
            
        } catch (IOException e) {
            System.out.println(e);
            System.err.println(".env file not found or unreadable.");
        }
    }
}