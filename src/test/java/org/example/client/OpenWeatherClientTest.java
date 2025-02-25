package org.example.client;

import org.example.dto.Weather;
import org.example.exceptions.GetWeatherException;
import org.example.exceptions.InOutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenWeatherClientTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private OpenWeatherClient openWeatherClient;

    private final String jsonResponse = """
            {
                "main": {
                    "temp": 285.32,
                    "feels_like": 283.4,
                    "temp_min": 284.15,
                    "temp_max": 286.48,
                    "humidity": 82
                },
                "wind": {
                    "speed": 4.6
                },
                "weather": [
                    { "description": "clear sky" }
                ]
            }
            """;

    @BeforeEach
    void setUp() throws Exception {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
    }

    @Test
    void testGetWeatherSuccess() {
        when(httpResponse.body()).thenReturn(jsonResponse);

        Weather weather = openWeatherClient.getWeather("miami", "testApiKey");

        assertNotNull(weather);
        assertEquals("miami", weather.getCity());
        assertEquals("clear sky", weather.getPhenomenon());
        assertEquals(285.32, weather.getTemperature());
        assertEquals(283.4, weather.getFeelsLike());
        assertEquals(284.15, weather.getTemperatureMin());
        assertEquals(286.48, weather.getTemperatureMax());
        assertEquals(4.6, weather.getWind());
        assertEquals(82, weather.getHumidity());
    }

    @Test
    void testGetWeatherThrowsGetWeatherExceptionOnEmptyResponse() {
        when(httpResponse.body()).thenReturn("");

        assertThrows(GetWeatherException.class, () -> openWeatherClient.getWeather("miami", "testApiKey"));
    }

    @Test
    void testGetWeatherThrowsInOutExceptionOnIOException() throws Exception {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Network error"));

        assertThrows(InOutException.class, () -> openWeatherClient.getWeather("miami", "testApiKey"));
    }
}