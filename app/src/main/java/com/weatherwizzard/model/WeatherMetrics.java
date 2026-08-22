package com.weatherwizzard.model;

/**
 * Immutable data model representing next-day weather forecast metrics for a single city.
 */
public record WeatherMetrics(
        String date,
        double minTempC,
        double maxTempC,
        int avgHumidity,
        double maxWindKph,
        String windDir
) {}
