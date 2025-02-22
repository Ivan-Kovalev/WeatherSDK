package org.example.sdk.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.example.client.OpenWeatherClient;
import org.example.dto.ModeWeatherSDK;
import org.example.dto.Weather;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.example.dto.ModeWeatherSDK.POLLING;
import static org.example.dto.ModeWeatherSDK.REQUEST;

public final class WeatherSDK {
    private final String apiKey;
    private final ModeWeatherSDK mode;
    private final OpenWeatherClient client = new OpenWeatherClient();
    private final Cache<String, Weather> weatherCache;

    public WeatherSDK(ModeWeatherSDK mode, String apiKey) {
        this.mode = mode;
        this.apiKey = apiKey;

        this.weatherCache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    public ModeWeatherSDK getMode() {
        return mode;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Weather getWeather(String city) {
        Weather cachedWeather = weatherCache.getIfPresent(city);

        if (mode == POLLING) {
            Weather fetchedWeather = client.getWeather(city, apiKey);
            weatherCache.put(city, fetchedWeather);
            CompletableFuture.runAsync(() -> refreshAllWeather(apiKey));
            return cachedWeather;
        }

        if (mode == REQUEST) {
            Weather result = client.getWeather(city, apiKey);
            weatherCache.put(city, result);
            return result;
        } else {
            throw new UnsupportedOperationException("Unsupported mode");
        }
    }

    private void refreshAllWeather(String apiKey) {
        for (String city : weatherCache.asMap().keySet()) {
            Weather updatedWeather = client.getWeather(city, apiKey);
            weatherCache.put(city, updatedWeather);
        }
    }
}
