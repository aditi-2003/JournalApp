package com.aditi.journal_app.service;

import com.aditi.journal_app.model.JournalEntry;
import com.aditi.journal_app.repository.JournalRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalService {

    private final JournalRepository journalRepository;
    private final WeatherService weatherService;

    public JournalService(JournalRepository journalRepository, WeatherService weatherService) {
        this.journalRepository = journalRepository;
        this.weatherService = weatherService;
    }

    @Cacheable(value = "journalEntries", key = "#username")
    public List<JournalEntry> getEntriesByUser(String username) {
        return journalRepository.findByUserUsername(username);
    }

    @CacheEvict(value = "journalEntries", key = "#entry.user.username")
    public JournalEntry addEntry(JournalEntry entry) {
        // Fetch weather if city is provided
        if (entry.getCity() != null && !entry.getCity().isEmpty()) {
            var weather = weatherService.getWeather(entry.getCity());
            entry.setTemperature(weather.getTemperature());
            entry.setWeatherDesc(weather.getDescription());
        }
        return journalRepository.save(entry);
    }
}
