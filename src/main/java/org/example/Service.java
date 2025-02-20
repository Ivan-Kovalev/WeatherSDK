package org.example;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Service {



    public Weather getWeather(String city, String apiKey) {
        String output = getUrlContent("http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey);

        if (!output.isEmpty()) {
            JSONObject object = new JSONObject(output);
            return new Weather(
                    city,
                    object.getJSONObject("weather").getString("description"),
                    object.getJSONObject("main").getDouble("temp"),
                    object.getJSONObject("main").getDouble("feels_like"),
                    object.getJSONObject("main").getDouble("temp_min"),
                    object.getJSONObject("main").getDouble("temp_max"),
                    object.getJSONObject("wind").getDouble("speed"),
                    object.getJSONObject("main").getDouble("humidity")
            );
        } else {
            //тут планирую возвращать ошибки в случае невалидных данных
            return null;
        }
    }

    private static String getUrlContent(String urlAddress) {
        StringBuffer content = new StringBuffer();

        // блок try/catch планирую убрать что бы ошибки обрабатывались в контроллере
        try {
            URL url = new URL(urlAddress);
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("Такой город не был найден!");
        }
        return content.toString();
    }
}
