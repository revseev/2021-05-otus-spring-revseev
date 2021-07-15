package ru.revseev.library.shell.dto

data class NewBookDto(val title: String, val authorName: String, val genres: List<GenreDto>)