package com.example.first1.Entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "journal")
data class JournalEntry(
    @Id
    var id: String? = null,
    var journalId: String? = null,
    var journalTitle: String? = null,
    var journalContent: String? = null
)
