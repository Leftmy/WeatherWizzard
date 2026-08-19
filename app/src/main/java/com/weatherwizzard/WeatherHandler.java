package com.weatherwizzard;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class WeatherHandler{
    /*public Dictionary weatherData;*/

    public static URI createUri(String city, String apiKey) throws URISyntaxException {
        String scheme = "http";
        String host = "api.weatherapi.com";
        String path = "/v1/forecast.json";
        String query = "key=" + apiKey + "&q=" + city + "&days=" + 2;

        return new URI(scheme, host, path, query, null);
    }

    public void fetchData(String apiKey, String city){

        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("API key is missing!");
            return;
        }

        try{
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
            .uri(createUri(city, apiKey))
            .header("Accept", "application/json")
            .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body:\n" + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}