package org.example.sdk;

import org.example.dto.ModeWeatherSDK;
import org.example.dto.Weather;

public interface WeatherSDK {
    ModeWeatherSDK getMode();

    String getApiKey();

    Weather getWeather(String city);

    void shutdown();
}
