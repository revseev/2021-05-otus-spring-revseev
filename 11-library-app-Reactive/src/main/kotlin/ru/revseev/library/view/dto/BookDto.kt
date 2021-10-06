package ru.revseev.library.view.dto

data class BookDto(
    val id: String? = null,
    val title: String = "",
    val authorName: String = "",
    var genres: String = "",
)
