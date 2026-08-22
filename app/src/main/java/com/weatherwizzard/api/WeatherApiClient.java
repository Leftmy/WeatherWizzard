package com.weatherwizzard.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Responsible solely for HTTP communication with the WeatherAPI.com REST endpoint.
 * Returns raw JSON strings; all parsing is delegated to {@link WeatherParser}.
 */
public class WeatherApiClient {

    private final HttpClient httpClient;

    public WeatherApiClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Fetches raw JSON forecast for the given city.
     *
     * @throws IOException          on non-200 HTTP status or I/O failure
     * @throws InterruptedException if the request is interrupted
     * @throws URISyntaxException   if the constructed URI is invalid
     */
    public String fetchRawForecast(String city, String apiKey)
            throws IOException, InterruptedException, URISyntaxException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(buildUri(city, apiKey))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException(
                    "Unexpected HTTP status " + response.statusCode() + " for city: " + city);
        }

        return response.body();
    }

    /**
     * Package-private so it can be tested directly without making network calls.
     */
    static URI buildUri(String city, String apiKey) throws URISyntaxException {
        String query = "key=" + apiKey + "&q=" + city + "&days=2";
        return new URI("https", "api.weatherapi.com", "/v1/forecast.json", query, null);
    }
}
