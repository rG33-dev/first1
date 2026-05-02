package com.example.first1.services;

import com.example.first1.Entity.User;
import com.example.first1.JournalEntryRepo.uRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserEntryService {
    private final uRepo userRepo;

    public UserEntryService(uRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User saveUserEntry(User user) {
        return userRepo.save(user);
    }

    public List<User> getAllUserEntries() {
        return userRepo.findAll();
    }

    public List<User> findAllUserEntries() {
        return userRepo.findAll();
    }

    public Optional<User> findUserEntryById(String id) {
        return userRepo.findById(id);
    }

    public Optional<User> findUserEntryByName(String name) {
        return userRepo.findByUsername(name);
    }

    public User updateUser(String username, User user) {
        Optional<User> existingUser = userRepo.findByUsername(username);
        if (existingUser.isEmpty()) {
            return null;
        }
        User userInDb = existingUser.get();
        user.setId(userInDb.getId());
        user.setUsername(username);
        user.getJournalEntries();
        return userRepo.save(user);
    }

    public boolean deleteUserEntryByUsername(String username) {
        Optional<User> existingUser = userRepo.findByUsername(username);
        if (existingUser.isEmpty()) {
            return false;
        }
        userRepo.delete(existingUser.get());
        return true;
    }
}
