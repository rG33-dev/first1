package com.example.first1.services.JournalEntryRepo;

import com.example.first1.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface  uRepo  extends MongoRepository<User,ObjectId> {
}
