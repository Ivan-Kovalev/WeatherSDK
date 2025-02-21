package org.example.sdk;

import org.example.dto.ModeWeatherSDK;
import org.example.dto.Weather;

import java.io.IOException;
import java.net.URISyntaxException;

public interface WeatherSDK {
    ModeWeatherSDK getMode();

    String getApiKey();

    Weather getWeather(String city) throws IOException, URISyntaxException, InterruptedException;
}
