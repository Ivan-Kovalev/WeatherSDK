package org.example.sdk.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.example.client.OpenWeatherClient;
import org.example.dto.ModeWeatherSDK;
import org.example.dto.Weather;
import org.example.sdk.WeatherSDK;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.dto.ModeWeatherSDK.POLLING;

public final class WeatherSDKImpl implements WeatherSDK {
    private final String apiKey;
    private final ModeWeatherSDK mode;
    private final OpenWeatherClient client = new OpenWeatherClient();
    private final Cache<String, Weather> weatherCache;
    private static final Logger logger = Logger.getLogger(WeatherSDKImpl.class.getName());

    public WeatherSDKImpl(ModeWeatherSDK mode, String apiKey) {
        this.mode = mode;
        this.apiKey = apiKey;

        this.weatherCache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public ModeWeatherSDK getMode() {
        return mode;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public Weather getWeather(String city) throws IOException, URISyntaxException, InterruptedException {
        Weather cachedWeather = weatherCache.getIfPresent(city);

        if (mode == POLLING) {
            try {
                refreshAllWeather(apiKey);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Input/output error: ", e);
            } catch (URISyntaxException e) {
                logger.log(Level.SEVERE, "URI syntax error: ", e);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "The flow was interrupted: ", e);
            }
        }

        if (cachedWeather != null) {
            return cachedWeather;
        }

        if (mode == POLLING) {
            Weather result = client.getWeather(city, apiKey);
            weatherCache.put(city, result);
            return result;
        } else {
            return null;
        }
    }

    private void refreshAllWeather(String apiKey) throws IOException, URISyntaxException, InterruptedException {
        for (String city : weatherCache.asMap().keySet()) {
            weatherCache.put(city, client.getWeather(city, apiKey));
        }
    }
}
