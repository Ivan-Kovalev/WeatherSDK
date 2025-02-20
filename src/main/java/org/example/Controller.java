package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Controller {

    private final Service service;

    public Controller(Service service) {
        this.service = service;
    }

    public Weather getWeather(String city, String apiKey) {
        return service.getWeather(city, apiKey);
    }

}
