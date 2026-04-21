package com.example.first1.controller;

import com.example.first1.Entity.JournalEntry;
import com.example.first1.services.JournalEntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController2 {
    private final JournalEntryService journalEntryService;

    public JournalEntryController2(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalEntries() {
        return ResponseEntity.ok(journalEntryService.findAll());
    }

    @PostMapping
    public ResponseEntity<JournalEntry> saveJournalEntry(@RequestBody JournalEntry journalEntry) {
        JournalEntry savedEntry = journalEntryService.saveEntry(journalEntry);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntry);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String id) {
        return journalEntryService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteJournalEntryById(@PathVariable String id) {
        boolean deleted = journalEntryService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntry(
            @PathVariable String id,
            @RequestBody JournalEntry journalEntry
    ) {
        JournalEntry updatedEntry = journalEntryService.updateEntry(id, journalEntry);
        if (updatedEntry == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedEntry);
    }
}
