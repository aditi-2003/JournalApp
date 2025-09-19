package com.aditi.journal_app.dto;

public class WeatherDTO {
    private String city;
    private double temperature;

    public WeatherDTO(String city, double temperature, String description) {
        this.city = city;
        this.temperature = temperature;
        this.description = description;
    }

    public WeatherDTO() {}

    private String description;

    public String getCity() {return city;}
    public void setCity(String city) {this.city = city;}

    public double getTemperature() {return temperature;}
    public void setTemperature(double temperature) {this.temperature = temperature;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}

