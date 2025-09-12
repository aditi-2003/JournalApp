package com.aditi.journal_app.controller;

import com.aditi.journal_app.dto.JournalEntryDTO;
import com.aditi.journal_app.model.JournalEntry;
import com.aditi.journal_app.model.User;
import com.aditi.journal_app.repository.JournalRepository;
import com.aditi.journal_app.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/journals")
public class JournalController {

    private final JournalRepository journalRepository;
    private final UserRepository userRepository;

    public JournalController(JournalRepository journalRepository, UserRepository userRepository) {
        this.journalRepository = journalRepository;
        this.userRepository = userRepository;
    }

    // Add journal for current user
    @PostMapping
    public JournalEntryDTO createJournal(@RequestBody JournalEntry journal, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        journal.setUser(user);
        JournalEntry saved = journalRepository.save(journal);
        return new JournalEntryDTO(saved); // return DTO
    }

    // Get current user's journals
    @GetMapping
    public List<JournalEntryDTO> getUserJournals(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return journalRepository.findByUser(user).stream()
                .map(JournalEntryDTO::new) // ✅ map each entity to DTO
                .collect(Collectors.toList());
    }

    // Delete journal if it belongs to current user
    @DeleteMapping("/{id}")
    public String deleteJournal(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        JournalEntry journal = journalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        if (!journal.getUser().getId().equals(user.getId())) {
            return "Not allowed!";
        }
        journalRepository.delete(journal);
        return "Deleted!";
    }

    @PutMapping("/{id}/share")
    public String shareJournal(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        JournalEntry journal = journalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        if (!journal.getUser().getId().equals(user.getId())) {
            return "Not allowed!";
        }

        journal.setShared(true);
        journalRepository.save(journal);
        return "Journal shared successfully!";
    }


}

