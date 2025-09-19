package com.aditi.journal_app.dto;

import com.aditi.journal_app.model.JournalEntry;
import java.time.LocalDateTime;

public class JournalEntryDTO {
    private Long id;
    private String title;
    private String content;
    private String username;
    private boolean shared;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Weather fields
    private String city;
    private Double temperature;
    private String weatherDesc;

    // Constructor using JournalEntry entity
    public JournalEntryDTO(JournalEntry entry) {
        this.id = entry.getId();
        this.title = entry.getTitle();
        this.content = entry.getContent();
        this.username = entry.getUser().getUsername();
        this.shared = entry.isShared();
        this.createdAt = entry.getCreatedAt();
        this.updatedAt = entry.getUpdatedAt();
        this.city = entry.getCity();
        this.temperature = entry.getTemperature();
        this.weatherDesc = entry.getWeatherDesc();
    }

    // Full constructor
    public JournalEntryDTO(Long id, String title, String content, String username,
                           boolean shared, LocalDateTime createdAt, LocalDateTime updatedAt,
                           String city, Double temperature, String weatherDesc) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.shared = shared;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.city = city;
        this.temperature = temperature;
        this.weatherDesc = weatherDesc;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getUsername() { return username; }
    public boolean getShared() { return shared; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public String getCity() { return city; }
    public Double getTemperature() { return temperature; }
    public String getWeatherDesc() { return weatherDesc; }
}



