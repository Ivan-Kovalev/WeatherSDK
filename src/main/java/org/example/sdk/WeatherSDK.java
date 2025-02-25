package org.example.sdk;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.example.client.OpenWeatherClient;
import org.example.dto.ModeWeatherSDK;
import org.example.dto.Weather;
import org.example.exceptions.UpdateWeatherException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.example.dto.ModeWeatherSDK.POLLING;
import static org.example.dto.ModeWeatherSDK.REQUEST;

public final class WeatherSDK {

    private final String apiKey;
    private final ModeWeatherSDK mode;

    private final Cache<String, Weather> weatherCache;
    private final OpenWeatherClient client = new OpenWeatherClient();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public WeatherSDK(ModeWeatherSDK mode, String apiKey) {
        this.mode = mode;
        this.apiKey = apiKey;

        this.weatherCache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
        if (mode == ModeWeatherSDK.POLLING) {
            startWeatherRefreshScheduler();
        }
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
            weatherCache.put(city, client.getWeather(city, apiKey));
            return weatherCache.getIfPresent(city);
        }

        if (cachedWeather != null) {
            return cachedWeather;
        }

        if (mode == REQUEST) {
            Weather result = client.getWeather(city, apiKey);
            weatherCache.put(city, result);
            return weatherCache.getIfPresent(city);
        } else {
            throw new UnsupportedOperationException("Unsupported mode");
        }
    }

    private void refreshAllWeather() {
        for (String city : weatherCache.asMap().keySet()) {
            try {
                Weather updatedWeather = client.getWeather(city, apiKey);
                weatherCache.put(city, updatedWeather);
            } catch (Exception e) {
                throw new UpdateWeatherException("Failed to update weather for " + city + ": " + e.getMessage());
            }
        }
    }

    private void startWeatherRefreshScheduler() {
        scheduler.scheduleAtFixedRate(this::refreshAllWeather, 0, 1, TimeUnit.MINUTES);
    }

}
