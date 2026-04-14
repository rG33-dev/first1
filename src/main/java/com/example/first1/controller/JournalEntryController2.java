package com.example.first1.controller;


import com.example.first1.Entity.JournalEntry;
import com.example.first1.services.JournalEntryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/_journal")
public class JournalEntryController2 {

    private final JournalEntryService journalEntryService;

    public JournalEntryController2(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    @GetMapping("/journal")
    public List<JournalEntry> getAll() {
        // Bug in the old code: it returned controller instances instead of journal data.
        return journalEntryService.getAll();
    }

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry journalEntry) {
        // Bug in the old code: the request body was the controller type, which Spring should never persist.
        return journalEntryService.saveEntry(journalEntry);
    }

    @GetMapping("/id/{id}")
    public Optional<JournalEntry> getJournalEntry(@PathVariable String id) {
        // Bug in the old code: route variable name (`myId`) and parameter name (`id`) did not match.
        return journalEntryService.getById(id);
    }

    @DeleteMapping("/id/{id}")
    public void deleteEntry(@PathVariable String id) {
        journalEntryService.deleteById(id);
    }

    @PutMapping("/id/{id}")
    public JournalEntry updateEntry(@PathVariable String id, @RequestBody JournalEntry journalEntry) {
        // Bug in the old code: Spring MVC accepts one request body, not two.
        journalEntry.setId(id);
        return journalEntryService.saveEntry(journalEntry);
    }
}

