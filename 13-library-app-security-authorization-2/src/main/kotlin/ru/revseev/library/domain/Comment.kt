package ru.revseev.library.domain

import org.springframework.data.mongodb.core.mapping.Document

@Document("comments")
data class Comment(
    val bookId: String,
    var body: String = "",
) : StringIdentifiable()