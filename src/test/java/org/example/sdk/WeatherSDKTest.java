package org.example.sdk;

import org.example.client.OpenWeatherClient;
import org.example.dto.ModeWeatherSDK;
import org.example.dto.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherSDKTest {

    private static final String API_KEY = "35daaca6d996a09e0d47bd8ebd830590";
    private static final String CITY = "miami";

    @Mock
    private OpenWeatherClient client;

    @InjectMocks
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
        weatherSDK = new WeatherSDK(ModeWeatherSDK.REQUEST, API_KEY);
        Mockito.when(client.getWeather(CITY, API_KEY)).thenReturn(testWeather);

        Weather weather = weatherSDK.getWeather(CITY);
        assertNotNull(weather);
        assertEquals("clear sky", weather.getPhenomenon());
        verify(client, times(1)).getWeather(CITY, API_KEY);
    }

    @Test
    void testGetWeather_PollingMode_CacheHit() {
        weatherSDK = new WeatherSDK(ModeWeatherSDK.POLLING, API_KEY);
        weatherSDK.getWeather(CITY); // Заполним кэш
        when(client.getWeather(CITY, API_KEY)).thenReturn(testWeather);

        Weather weather = weatherSDK.getWeather(CITY);
        assertNotNull(weather);
        verify(client, times(1)).getWeather(CITY, API_KEY);
    }

    @Test
    void testGetWeather_PollingMode_CacheMiss() {
        weatherSDK = new WeatherSDK(ModeWeatherSDK.POLLING, API_KEY);
        when(client.getWeather(CITY, API_KEY)).thenReturn(testWeather);

        Weather weather = weatherSDK.getWeather(CITY);
        assertNotNull(weather);
        assertEquals("clear sky", weather.getPhenomenon());
        verify(client, times(1)).getWeather(CITY, API_KEY);
    }

    @Test
    void testUnsupportedMode() {
        weatherSDK = new WeatherSDK(null, API_KEY);
        assertThrows(UnsupportedOperationException.class, () -> weatherSDK.getWeather(CITY));
    }
}