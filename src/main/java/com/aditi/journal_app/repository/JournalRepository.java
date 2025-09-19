package com.aditi.journal_app.repository;

import com.aditi.journal_app.model.JournalEntry;
import com.aditi.journal_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByUser(User user);

    List<JournalEntry> findByUserUsername(String username);

    Optional<JournalEntry> findTopByUserUsernameOrderByCreatedAtDesc(String username);
}
