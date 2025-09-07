package com.aditi.journal_app.dto;

import com.aditi.journal_app.model.JournalEntry;

public class JournalEntryDTO {
    private Long id;
    private String title;
    private String content;
    private String username;

    // Constructor using JournalEntry entity
    public JournalEntryDTO(JournalEntry entry) {
        this.id = entry.getId();
        this.title = entry.getTitle();
        this.content = entry.getContent();
        this.username = entry.getUser().getUsername(); // only safe field
    }

    // Getters (you can use Lombok @Getter to reduce boilerplate)
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getUsername() { return username; }
}

