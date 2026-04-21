package com.example.first1.Entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    var id: String? = null,
    @field:Indexed(unique = true)
    var username: String = "",
    var password: String = "",
    @DBRef
    var journalEntries: MutableList<JournalEntry> = mutableListOf()
)
