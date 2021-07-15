package ru.revseev.library.shell

import ru.revseev.library.shell.dto.GenreDto

interface GenreParser {

    fun parseGenres(genres: String): MutableList<GenreDto>
}
