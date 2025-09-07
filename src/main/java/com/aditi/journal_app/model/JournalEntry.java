package com.aditi.journal_app.model;


import jakarta.persistence.*;

@Entity
@Table(name = "journalentry")
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000) // allow longer journal content
    private String content;

    // ✅ Many entries belong to ONE user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // foreign key in journalentry table
    private User user;

    public JournalEntry() {}

    public JournalEntry(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}


