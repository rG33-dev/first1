package com.example.first1.services;

import com.example.first1.Entity.JournalEntry;
import com.example.first1.JournalEntryRepo.Repo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {
    private final Repo repo;

    public JournalEntryService(Repo repo) {
        this.repo = repo;
    }

    public JournalEntry saveEntry(JournalEntry journalEntry) {
        return repo.save(journalEntry);
    }

    public List<JournalEntry> findAll() {
        return repo.findAll();
    }

    public Optional<JournalEntry> findById(String id) {
        return repo.findById(id);
    }

    public boolean deleteById(String id) {
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }

    public JournalEntry updateEntry(String id, JournalEntry journalEntry) {
        if (!repo.existsById(id)) {
            return null;
        }
        journalEntry.setId(id);
        return repo.save(journalEntry);
    }
}
