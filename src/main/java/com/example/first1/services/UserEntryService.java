package com.example.first1.services;

import com.example.first1.Entity.JournalEntry;
import com.example.first1.Entity.User;
import com.example.first1.services.JournalEntryRepo.Repo;
import com.example.first1.services.JournalEntryRepo.uRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class UserEntryService {

    @Autowired
     private uRepo userRepo;

   public void saveUserEntry(User user, JournalEntry journalEntry){
       userRepo.save(user);
   }

}
