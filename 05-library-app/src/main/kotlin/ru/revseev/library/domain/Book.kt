package ru.revseev.library.domain

data class Book(
    val id: Long? = null,
    val title: String,
    val author: Author,
    val genres: MutableList<Genre> = mutableListOf()
)