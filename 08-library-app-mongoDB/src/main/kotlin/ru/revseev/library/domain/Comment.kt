package ru.revseev.library.domain

import org.springframework.data.mongodb.core.mapping.Document

@Document( "comments")
class Comment(
    val book: Book,
    var body: String = "",
) : LongIdentifiable() {

}