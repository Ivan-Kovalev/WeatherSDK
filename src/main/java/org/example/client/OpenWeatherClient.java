package org.example.client;

import org.example.dto.Weather;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OpenWeatherClient {

    private static String getUrlContent(String urlAddress) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlAddress))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public Weather getWeather(String city, String apiKey) throws IOException, URISyntaxException, InterruptedException {
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
            return null;
        }
    }
}
