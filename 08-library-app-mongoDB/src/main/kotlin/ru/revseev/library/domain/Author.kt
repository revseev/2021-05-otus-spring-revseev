package ru.revseev.library.domain

import org.springframework.data.mongodb.core.mapping.Document

@Document("authors")
class Author(
    val name: String,
) : LongIdentifiable() {

}

