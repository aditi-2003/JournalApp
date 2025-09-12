package com.aditi.journal_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

//    @JsonIgnore
    private String password;

    // Roles stored in a separate table
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<String> roles = new HashSet<>();

    //  One user can have many journal entries
//    cascade = CascadeType.ALL → if a user is deleted, their journal entries are also deleted.
//    orphanRemoval = true → if a journal entry is removed from the set, it’s also removed from DB.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
    private Set<JournalEntry> journalEntries = new HashSet<>();

    public User() {}

    public User(String username, String password, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    // --- getters & setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public Set<JournalEntry> getJournalEntries() { return journalEntries; }
    public void setJournalEntries(Set<JournalEntry> journalEntries) { this.journalEntries = journalEntries; }
}

