package ru.revseev.library.domain

import org.springframework.data.mongodb.core.mapping.Document

@Document("books")
data class Book(
    var title: String,
    val author: Author,
    var genres: MutableList<Genre> = mutableListOf(),
) : StringIdentifiable() {

    var commentIds: MutableList<String> = mutableListOf()
}