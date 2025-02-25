package org.example.sdk;

import org.example.client.OpenWeatherClient;
import org.example.dto.ModeWeatherSDK;
import org.example.dto.Weather;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherSDKTest {

    private static final String API_KEY = "35daaca6d996a09e0d47bd8ebd830590";
    private static final String CITY = "miami";

    private WeatherSDK weatherSDK;

    private Weather testWeather = new Weather(
            CITY,
            "clear sky",
            20.5,
            18.0,
            15.0,
            25.0,
            5.0,
            65.0);


    @Test
    void testGetWeather_RequestMode() {
        OpenWeatherClient client = Mockito.mock(OpenWeatherClient.class);
        weatherSDK = new WeatherSDK(ModeWeatherSDK.REQUEST, API_KEY, client);
        Mockito.when(client.getWeather(CITY, API_KEY)).thenReturn(testWeather);

        Weather weather = weatherSDK.getWeather(CITY);
        assertNotNull(weather);
        assertEquals("clear sky", weather.getPhenomenon());
        verify(client, times(1)).getWeather(CITY, API_KEY);
    }

    @Test
    void testGetWeather_PollingMode_CacheHit() {
        OpenWeatherClient client = Mockito.mock(OpenWeatherClient.class);
        weatherSDK = new WeatherSDK(ModeWeatherSDK.POLLING, API_KEY, client);
        when(client.getWeather(CITY, API_KEY)).thenReturn(testWeather);

        weatherSDK.getWeather(CITY);
        Weather cachedWeather = weatherSDK.getWeather(CITY);

        assertNotNull(cachedWeather);
        assertEquals(testWeather, cachedWeather);
    }

    @Test
    void testGetWeather_PollingMode_CacheMiss() {
        OpenWeatherClient client = Mockito.mock(OpenWeatherClient.class);
        weatherSDK = new WeatherSDK(ModeWeatherSDK.POLLING, API_KEY, client);
        when(client.getWeather(CITY, API_KEY)).thenReturn(testWeather);

        Weather weather = weatherSDK.getWeather(CITY);
        assertNotNull(weather);
        assertEquals("clear sky", weather.getPhenomenon());
        verify(client, times(1)).getWeather(CITY, API_KEY);
    }

    @Test
    void testUnsupportedMode() {
        OpenWeatherClient client = Mockito.mock(OpenWeatherClient.class);
        weatherSDK = new WeatherSDK(null, API_KEY, client);
        assertThrows(UnsupportedOperationException.class, () -> weatherSDK.getWeather(CITY));
    }
}