package org.example;

public class Main {
    public static void main(String[] args) {
        SDK sdk = new SDK("test", "35daaca6d996a09e0d47bd8ebd830590");

        System.out.println(sdk.getWeather("Киев"));
    }
}