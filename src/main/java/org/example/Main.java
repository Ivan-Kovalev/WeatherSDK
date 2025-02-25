package org.example;

import org.example.sdk.WeatherSDK;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.example.dto.ModeWeatherSDK.POLLING;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        WeatherSDK weatherSdk2 = new WeatherSDK(POLLING, "35daaca6d996a09e0d47bd8ebd830590");

        System.out.println(weatherSdk2.getWeather("miami"));
    }
}