package ru.revseev.library.domain

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Author(val name: String)

