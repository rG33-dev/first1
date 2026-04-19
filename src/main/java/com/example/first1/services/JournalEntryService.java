package com.example.first1.services;


import com.example.first1.services.JournalEntryRepo.Repo;
import com.example.first1.Entity.JournalEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class JournalEntryService {

    @Autowired
    private Repo repo;

    public void saveEntry(JournalEntry journalEntry) {
        repo.save(journalEntry);
    }

    public List<JournalEntry> findAll() {
        return repo.findAll();
    }

    public void findById(String id) {
        repo.findById(id);

    }

    public void deleteById(String id) {
        repo.deleteById(id);

    }






}
