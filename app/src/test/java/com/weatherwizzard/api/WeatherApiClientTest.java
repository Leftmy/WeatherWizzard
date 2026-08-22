package com.weatherwizzard.api;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link WeatherApiClient#buildUri}.
 * No network calls are made — buildUri is package-private and tested directly.
 */
public class WeatherApiClientTest {

    @Test
    public void buildUri_usesHttpsScheme() throws URISyntaxException {
        URI uri = WeatherApiClient.buildUri("Kyiv", "test-key");
        assertEquals("https", uri.getScheme());
    }

    @Test
    public void buildUri_containsCityInQuery() throws URISyntaxException {
        URI uri = WeatherApiClient.buildUri("Amsterdam", "test-key");
        assertTrue(uri.getQuery().contains("q=Amsterdam"));
    }

    @Test
    public void buildUri_containsApiKeyInQuery() throws URISyntaxException {
        URI uri = WeatherApiClient.buildUri("Madrid", "my-secret-key");
        assertTrue(uri.getQuery().contains("key=my-secret-key"));
    }

    @Test
    public void buildUri_requestsTwoDays() throws URISyntaxException {
        URI uri = WeatherApiClient.buildUri("Chisinau", "test-key");
        assertTrue(uri.getQuery().contains("days=2"));
    }

    @Test
    public void buildUri_hasCorrectHost() throws URISyntaxException {
        URI uri = WeatherApiClient.buildUri("Kyiv", "test-key");
        assertEquals("api.weatherapi.com", uri.getHost());
    }

    @Test
    public void buildUri_hasCorrectPath() throws URISyntaxException {
        URI uri = WeatherApiClient.buildUri("Kyiv", "test-key");
        assertEquals("/v1/forecast.json", uri.getPath());
    }
}
