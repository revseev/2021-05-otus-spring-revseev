package ru.revseev.library.view

import ru.revseev.library.view.dto.GenreDto

interface GenreParser {

    fun parseGenres(genres: String): MutableList<GenreDto>
}
