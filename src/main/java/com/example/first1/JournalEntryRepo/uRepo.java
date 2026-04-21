package com.example.first1.JournalEntryRepo;

import com.example.first1.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface uRepo extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

}
