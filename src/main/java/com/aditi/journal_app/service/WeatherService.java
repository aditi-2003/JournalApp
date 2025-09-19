package com.aditi.journal_app.service;

import com.aditi.journal_app.dto.WeatherDTO;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Service
public class WeatherService {

    private final WebClient webClient;
    private final String apiKey = "f9740661242722c43066cc949f4af907"; // replace with your API key

    public WeatherService(WebClient.Builder webClientBuilder) {
        // WebClient with DNS timeout
        this.webClient = webClientBuilder
                .baseUrl("https://api.openweathermap.org/data/2.5")
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .resolver(DefaultAddressResolverGroup.INSTANCE)
                                .responseTimeout(Duration.ofSeconds(10))
                ))
                .build();
    }



    @Cacheable(value = "weather", key = "#city")
    public WeatherDTO getWeather(String city) {
//        try {
            // Fetch raw JSON as String
            String response = webClient.get()
                    .uri("/weather?q={city}&appid={key}&units=metric", city, apiKey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response == null) {
                return new WeatherDTO(city, 25.0, "Unavailable");
            }

            JSONObject json = new JSONObject(response);

            String name = json.optString("name", city);
            JSONObject main = json.optJSONObject("main");
            double temp = main != null ? main.optDouble("temp", 25.0) : 25.0;

            JSONArray weatherArray = json.optJSONArray("weather");
            String description = "Unavailable";
            if (weatherArray != null && weatherArray.length() > 0) {
                JSONObject w = weatherArray.getJSONObject(0);
                description = w.optString("description", "Unavailable");
            }

            return new WeatherDTO(name, temp, description);

//        } catch (Exception e) {
//            // fallback if API fails
//            return new WeatherDTO(city, 25.0, "Unavailable");
//        }
    }
}


