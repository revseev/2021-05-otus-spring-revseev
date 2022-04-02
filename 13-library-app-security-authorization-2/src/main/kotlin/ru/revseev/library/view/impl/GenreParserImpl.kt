package ru.revseev.library.view.impl

import org.springframework.stereotype.Service
import ru.revseev.library.view.GenreParser
import ru.revseev.library.view.dto.GenreDto

@Service
class GenreParserImpl : GenreParser {

    override fun parseGenres(genres: String): MutableList<GenreDto> = genres.split(",")
        .asSequence()
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .distinct()
        .map { GenreDto(it) }
        .toMutableList()
}