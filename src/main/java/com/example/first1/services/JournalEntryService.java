package com.example.first1.services;


import com.example.first1.services.JournalEntryRepo.Repo;
import com.example.first1.Entity.JournalEntry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    private final Repo journalEntryRepo;

    public JournalEntryService(Repo journalEntryRepo) {
        this.journalEntryRepo = journalEntryRepo;
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepo.findAll();
    }

    public JournalEntry saveEntry(JournalEntry journalEntry) {
        return journalEntryRepo.save(journalEntry);
    }

    public Optional<JournalEntry> getById(String id) {
        return journalEntryRepo.findById(id);
    }

    public void deleteById(String id) {
        journalEntryRepo.deleteById(id);
    }
}
