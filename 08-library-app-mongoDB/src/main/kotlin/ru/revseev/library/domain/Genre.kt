package ru.revseev.library.domain

import org.springframework.data.mongodb.core.mapping.Document

@Document("genres")
data class Genre(val name: String)

