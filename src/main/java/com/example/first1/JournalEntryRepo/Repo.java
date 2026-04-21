package com.example.first1.JournalEntryRepo;

import com.example.first1.Entity.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Repo extends MongoRepository<JournalEntry, String> {
}
