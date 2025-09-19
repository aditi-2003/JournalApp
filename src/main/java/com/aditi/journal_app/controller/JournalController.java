package com.aditi.journal_app.controller;

import com.aditi.journal_app.dto.DashboardResponse;
import com.aditi.journal_app.dto.JournalEntryDTO;
import com.aditi.journal_app.model.JournalEntry;
import com.aditi.journal_app.model.User;
import com.aditi.journal_app.repository.JournalRepository;
import com.aditi.journal_app.repository.UserRepository;
import com.aditi.journal_app.service.GreetingService;
import com.aditi.journal_app.service.JournalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class JournalController {

    private final JournalRepository journalRepository;
    private final UserRepository userRepository;
    private final JournalService journalService;
    private final GreetingService greetingService;

    public JournalController(JournalRepository journalRepository, UserRepository userRepository, JournalService journalService, GreetingService greetingService) {
        this.journalRepository = journalRepository;
        this.userRepository = userRepository;
        this.journalService = journalService;
        this.greetingService = greetingService;
    }

    @GetMapping("/dashboard")
    public DashboardResponse getDashboard(Authentication authentication) {
        String username = authentication.getName();

        // Fetch last journal entry (if any)
        JournalEntry latest = journalRepository.findTopByUserUsernameOrderByCreatedAtDesc(username).orElse(null);

        // Greeting based on time + weather
        String greeting = greetingService.getGreeting(username, latest != null ? latest.getCity() : null);

        // Return combined response
        return new DashboardResponse(
                username,
                greeting,
                latest != null ? new JournalEntryDTO(latest) : null
        );
    }

    // Add journal for current user
    @PostMapping("/journals")
    public JournalEntryDTO createJournal(@RequestBody JournalEntry journal, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        journal.setUser(user);
        JournalEntry saved = journalService.addEntry(journal);
        return new JournalEntryDTO(saved);
    }

    // Get current user's journals
    @GetMapping("/journals")
    public List<JournalEntryDTO> getUserJournals(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return journalRepository.findByUser(user).stream()
                .map(JournalEntryDTO::new)
                .collect(Collectors.toList());
    }

    // Delete journal if it belongs to current user
    @DeleteMapping("/journals/{id}")
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

    @PutMapping("/journals/{id}/share")
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

    @PutMapping("/journals/{id}")
    public ResponseEntity<?> updateJournal(@PathVariable Long id,
                                           @RequestBody JournalEntry updatedEntry,
                                           Authentication authentication) {
        // Find logged-in user
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find journal by ID
        JournalEntry journal = journalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        // Ensure only the owner can update
        if (!journal.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed!");
        }

        // Update fields
        journal.setTitle(updatedEntry.getTitle());
        journal.setContent(updatedEntry.getContent());
        journal.setShared(updatedEntry.isShared()); // optional
        journal.setCity(updatedEntry.getCity());

        // updatedAt will auto-update if you added @PreUpdate
        JournalEntry saved = journalService.addEntry(journal);

        return ResponseEntity.ok(new JournalEntryDTO(saved));
    }



}

