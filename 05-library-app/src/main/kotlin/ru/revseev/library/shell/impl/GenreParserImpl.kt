package ru.revseev.library.shell.impl

import org.springframework.stereotype.Service
import ru.revseev.library.domain.Genre
import ru.revseev.library.shell.GenreParser

@Service
class GenreParserImpl : GenreParser {

    override fun parseGenres(genres: String): MutableList<Genre> = genres.split(",")
        .asSequence()
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .distinct()
        .map { Genre(name = it) }
        .toMutableList()
}