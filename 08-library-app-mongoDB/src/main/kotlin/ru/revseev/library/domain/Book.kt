package ru.revseev.library.domain

import org.springframework.data.mongodb.core.mapping.Document

@Document("books")
class Book(
    var title: String,
    val author: Author,
    var genres: MutableList<Genre> = mutableListOf(),
) : LongIdentifiable() {

    var comments: MutableList<Comment> = mutableListOf()
}