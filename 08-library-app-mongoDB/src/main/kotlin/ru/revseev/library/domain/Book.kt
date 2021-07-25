package ru.revseev.library.domain

import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.mongodb.core.mapping.Document

@Document("books")
data class Book(
    var title: String,
    val author: Author,
    var genres: MutableList<Genre> = mutableListOf(),
) : StringIdentifiable() {

    val commentIds: MutableList<String> = mutableListOf()

    @PersistenceConstructor
    constructor(
        title: String,
        author: Author,
        genres: MutableList<Genre>,
        commentIds: List<String>,
    ) : this(title, author, genres) {
        this.commentIds += commentIds
    }
}