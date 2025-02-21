package org.example;

import org.example.sdk.impl.WeatherSDKImpl;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.example.dto.ModeWeatherSDK.POLLING;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        WeatherSDKImpl weatherSdk = new WeatherSDKImpl(POLLING, "35daaca6d996a09e0d47bd8ebd830590");

        System.out.println(weatherSdk.getWeather("california"));
    }
}