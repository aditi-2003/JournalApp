package com.aditi.journal_app.controller;

import com.aditi.journal_app.dto.JournalEntryDTO;
import com.aditi.journal_app.model.JournalEntry;
import com.aditi.journal_app.model.User;
import com.aditi.journal_app.repository.JournalRepository;
import com.aditi.journal_app.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final JournalRepository journalRepository;
    private final UserRepository userRepository;

    public AdminController(JournalRepository journalRepository, UserRepository userRepository) {
        this.journalRepository = journalRepository;
        this.userRepository = userRepository;
    }

    // View all journals in the system
    @GetMapping("/journals")
    public List<JournalEntryDTO> getAllJournals() {
        return journalRepository.findAll().stream()
                .map(JournalEntryDTO::new) // use constructor that takes JournalEntry
                .collect(Collectors.toList());
    }

    // View journals of a specific user
    @GetMapping("/journals/{username}")
    public List<JournalEntryDTO> getUserJournals(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return journalRepository.findByUser(user).stream()
                .map(JournalEntryDTO::new)
                .collect(Collectors.toList());
    }

    // Delete any journal by ID
    @DeleteMapping("/journals/{id}")
    public String deleteJournal(@PathVariable Long id) {
        JournalEntry journal = journalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found"));
        journalRepository.delete(journal);
        return "Journal deleted by Admin!";
    }
}


