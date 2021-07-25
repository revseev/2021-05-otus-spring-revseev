package ru.revseev.library.domain

import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document

@Document("comments")
data class Comment(
    var body: String = "",
) : StringIdentifiable() {
    @Transient
    var book: Book? = null
}