package com.aditi.journal_app.dto;

import com.aditi.journal_app.model.JournalEntry;

public class JournalEntryDTO {
    private Long id;
    private String title;
    private String content;
    private String username;
    private boolean shared;

    // Constructor using JournalEntry entity
    public JournalEntryDTO(JournalEntry entry) {
        this.id = entry.getId();
        this.title = entry.getTitle();
        this.content = entry.getContent();
        this.username = entry.getUser().getUsername();
        this.shared = entry.isShared(); // ✅ FIXED
    }

    public JournalEntryDTO(Long id, String title, String content, String username, boolean shared) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.shared = shared;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getUsername() { return username; }
    public boolean getShared() { return shared; }
}


