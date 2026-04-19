package com.example.first1.Entity

import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Date

@Document(collection = "journal")
data class JournalEntry(
    @Id
    var id: String? = null,
    var journalId: String? = null,
    @NotNull
    var journalTitle: String? = null,
    var journalContent: String? = null,

    val date: Date? = null
)
