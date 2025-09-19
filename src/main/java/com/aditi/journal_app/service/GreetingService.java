package com.aditi.journal_app.service;

import com.aditi.journal_app.dto.WeatherDTO;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class GreetingService {

    private final WeatherService weatherService;



    public GreetingService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public String getGreeting(String username, String city) {
        String baseGreeting;

        int hour = LocalTime.now().getHour();
        if (hour < 12) {
            baseGreeting = "Good Morning";
        } else if (hour < 18) {
            baseGreeting = "Good Afternoon";
        } else {
            baseGreeting = "Good Evening";
        }

        // Weather info (optional if city is not provided)
        String weatherPart = "";
        if (city != null && !city.isEmpty()) {
            try {
                WeatherDTO weather = weatherService.getWeather(city);
                weatherPart = String.format(" It's currently %.1f°C with %s in %s.",
                        weather.getTemperature(),
                        weather.getDescription(),
                        weather.getCity());
            } catch (Exception e) {
                weatherPart = "";
            }
        }

        return String.format("%s, %s!%s", baseGreeting, username, weatherPart);
    }
}

