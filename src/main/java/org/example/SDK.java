package org.example;

import java.util.*;

public final class SDK {
    private final String name;
    private final String apiKey;
    private Controller controller = new Controller(new Service());
    private Map<String, Weather> cache = new HashMap<>();

    public SDK(String name, String apiKey) {
        this.name = name;
        this.apiKey = apiKey;
    }

    public String getName() {
        return name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Weather getWeather(String city) {
        if (cache.containsKey(city)) {
            return cache.get(city);
        } else {
            Weather result = controller.getWeather(city, apiKey);
            cache.put(city, result);
            return result;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SDK sdk = (SDK) o;
        return Objects.equals(name, sdk.name) && Objects.equals(apiKey, sdk.apiKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, apiKey);
    }
}
