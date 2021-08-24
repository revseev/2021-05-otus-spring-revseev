package ru.revseev.library.view.dto

data class NewBookDto(val title: String, val authorName: String, val genres: List<GenreDto>)