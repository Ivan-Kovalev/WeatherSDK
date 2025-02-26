package org.example.sdk.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.example.client.OpenWeatherClient;
import org.example.dto.ModeWeatherSDK;
import org.example.dto.Weather;
import org.example.exceptions.UpdateWeatherException;
import org.example.sdk.WeatherSDK;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.example.dto.ModeWeatherSDK.POLLING;
import static org.example.dto.ModeWeatherSDK.REQUEST;

/** Implementation of SDK interface for providing weather data */
public final class WeatherSDKImpl implements WeatherSDK {

    private final String apiKey;
    private final ModeWeatherSDK mode;

    private final Cache<String, Weather> weatherCache;
    private OpenWeatherClient client;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public WeatherSDKImpl(ModeWeatherSDK mode, String apiKey) {
        this.mode = mode;
        this.apiKey = apiKey;
        client = new OpenWeatherClient();

        this.weatherCache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
        if (mode == ModeWeatherSDK.POLLING) {
            startWeatherRefreshScheduler();
        }
    }

    public WeatherSDKImpl(ModeWeatherSDK mode, String apiKey, OpenWeatherClient client) {
        this(mode, apiKey);
        this.client = client;
    }

    @Override
    public ModeWeatherSDK getMode() {
        return mode;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Get weather in a given city.
     * Depending on the selected mode (Request or Polling) - returns
     * weather data or updates information in the cache and returns weather data.
     * @param city
     * @return weather
     */
    @Override
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

    /** Method for updating weather that is stored in the cache */
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

    /** Run the weather update method every minute */
    private void startWeatherRefreshScheduler() {
        scheduler.scheduleAtFixedRate(this::refreshAllWeather, 0, 1, TimeUnit.MINUTES);
    }

    /** Stopping the SDK and removing it */
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            scheduler.shutdownNow();
        }
    }

}
