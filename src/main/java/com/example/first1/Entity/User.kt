package com.example.first1.Entity

import lombok.Data
import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Indexed


@Document(collection = "users")
@Data
public class User {


    @NotNull
    private val username: String = "" //Add unique index here
    @NotNull
    private val password: String = ""

    @Id
    private val objectId: Int? = null




    @DBRef                                           //ACTS AS FOREIGN KEY
    private val journalEntries: MutableList<JournalEntry> = ArrayList()


}