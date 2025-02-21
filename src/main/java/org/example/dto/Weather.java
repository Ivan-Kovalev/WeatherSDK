package org.example.dto;

import java.util.Objects;

public class Weather {
    private String city;
    private String phenomenon;
    private Double temperature;
    private Double feelsLike;
    private Double temperatureMin;
    private Double temperatureMax;
    private Double wind;
    private Double humidity;

    public Weather(String city,
                   String phenomenon,
                   Double temperature, Double feelsLike,
                   Double temperatureMin,
                   Double temperatureMax,
                   Double wind,
                   Double humidity) {
        this.city = city;
        this.phenomenon = phenomenon;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.wind = wind;
        this.humidity = humidity;
    }

    public String getCity() {
        return city;
    }

    public String getPhenomenon() {
        return phenomenon;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getTemperatureMin() {
        return temperatureMin;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }

    public Double getWind() {
        return wind;
    }

    public Double getHumidity() {
        return humidity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather = (Weather) o;
        return Objects.equals(city, weather.city) && Objects.equals(phenomenon, weather.phenomenon) && Objects.equals(temperature, weather.temperature) && Objects.equals(wind, weather.wind) && Objects.equals(humidity, weather.humidity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, phenomenon, temperature, wind, humidity);
    }

    @Override
    public String toString() {
        return "Weather{" +
                "city='" + city + '\'' +
                ", phenomenon='" + phenomenon + '\'' +
                ", temperature=" + temperature +
                ", feelsLike=" + feelsLike +
                ", temperatureMin=" + temperatureMin +
                ", temperatureMax=" + temperatureMax +
                ", wind=" + wind +
                ", humidity=" + humidity +
                '}';
    }
}
