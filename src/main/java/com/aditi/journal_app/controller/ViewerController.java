package com.aditi.journal_app.controller;

import com.aditi.journal_app.dto.JournalEntryDTO;
import com.aditi.journal_app.model.JournalEntry;
import com.aditi.journal_app.repository.JournalRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/viewer")
public class ViewerController {

    private final JournalRepository journalRepository;

    public ViewerController(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    // View all shared journals
    @GetMapping("/journals/public")
    public List<JournalEntryDTO> getSharedJournals() {
        return journalRepository.findAll().stream()
                .filter(JournalEntry::isShared) // only shared ones
                .map(journal -> new JournalEntryDTO(
                        journal.getId(),
                        journal.getTitle(),
                        journal.getContent(),
                        journal.getUser().getUsername(),
                        journal.isShared()
                ))
                .collect(Collectors.toList());
    }
}

