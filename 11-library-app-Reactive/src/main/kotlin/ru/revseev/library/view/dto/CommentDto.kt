package ru.revseev.library.view.dto

data class CommentDto(
    var id: String? = null,
    var bookId: String = "",
    var body: String = "",
)