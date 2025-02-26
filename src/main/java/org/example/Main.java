package org.example;

import org.example.dto.ModeWeatherSDK;
import org.example.dto.Weather;
import org.example.sdk.WeatherSDK;
import org.example.sdk.impl.WeatherSDKImpl;

public class Main {
    public static void main(String[] args) {

        // Example of using SDK

        // Create an instance of the SDK object
        WeatherSDK sdkExampleRequest = new WeatherSDKImpl(ModeWeatherSDK.REQUEST, "35daaca6d996a09e0d47bd8ebd830590");

        // We call the method for getting the weather, passing the city we are interested in as a parameter
        Weather example1 = sdkExampleRequest.getWeather("miami");
        System.out.println(example1);

        // We finish working with SDK by calling the shutdown method
        sdkExampleRequest.shutdown();

        WeatherSDK sdkExamplePolling = new WeatherSDKImpl(ModeWeatherSDK.POLLING, "35daaca6d996a09e0d47bd8ebd830590");
        Weather example2 = sdkExamplePolling.getWeather("miami");
        System.out.println(example2);
        sdkExamplePolling.shutdown();

    }
}