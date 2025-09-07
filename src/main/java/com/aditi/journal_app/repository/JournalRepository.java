package com.aditi.journal_app.repository;

import com.aditi.journal_app.model.JournalEntry;
import com.aditi.journal_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByUser(User user);
}
