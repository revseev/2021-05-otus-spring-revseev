package ru.revseev.library.shell

import ru.revseev.library.domain.Genre

interface GenreParser {

    fun parseGenres(genres: String): MutableList<Genre>
}
