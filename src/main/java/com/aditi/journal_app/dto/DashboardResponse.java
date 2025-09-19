package com.aditi.journal_app.dto;

public class DashboardResponse {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public JournalEntryDTO getLatestEntry() {
        return latestEntry;
    }

    public void setLatestEntry(JournalEntryDTO latestEntry) {
        this.latestEntry = latestEntry;
    }

    private String username;
    private String greeting;
    private JournalEntryDTO latestEntry;

    public DashboardResponse(String username, String greeting, JournalEntryDTO latestEntry) {
        this.username = username;
        this.greeting = greeting;
        this.latestEntry = latestEntry;
    }
}
