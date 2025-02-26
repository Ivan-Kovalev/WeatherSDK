package org.example.client;

import org.example.dto.Weather;
import org.example.exceptions.FlowStopException;
import org.example.exceptions.GetWeatherException;
import org.example.exceptions.InOutException;
import org.example.exceptions.URIException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/** Class for sending requests via API and handling responses. */
public class OpenWeatherClient {

    private HttpClient client;

    OpenWeatherClient(HttpClient client) {
        this.client = client;
    }

    public OpenWeatherClient() {
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Processing the response from OpenWeatherMap.
     * Converting the response to a JSON object and DTO for subsequent transfer to the SDK.
     * @param city
     * @param apiKey
     * @return
     */
    public Weather getWeather(String city, String apiKey) {
        String output = getUrlContent("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey);

        if (output != null && !output.isEmpty()) {
            JSONObject object = new JSONObject(output);
            JSONObject main = object.getJSONObject("main");
            JSONObject wind = object.getJSONObject("wind");
            JSONArray weatherArray = object.getJSONArray("weather");
            JSONObject weather = weatherArray.getJSONObject(0);

            return new Weather(
                    city,
                    weather.getString("description"),
                    main.getDouble("temp"),
                    main.getDouble("feels_like"),
                    main.getDouble("temp_min"),
                    main.getDouble("temp_max"),
                    wind.getDouble("speed"),
                    main.getDouble("humidity")
            );
        } else {
            throw new GetWeatherException("Unsuccessful attempt to transfer data using client method!");
        }
    }

    /**
     * Sending a request and receiving a response from OpenWeatherMap.
     * @param urlAddress
     * @return the entire page output in a String.
     */
    private static String getUrlContent(String urlAddress) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(urlAddress))
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new URIException("Incorrect URI syntax: " + e);
        }

        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new InOutException("Input/output error: " + e);
        } catch (InterruptedException e) {
            throw new FlowStopException("The flow was interrupted: " + e);
        }
        return response.body();
    }
}
