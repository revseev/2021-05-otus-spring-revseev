package ru.revseev.library.view.dto

import org.springframework.format.Formatter
import org.springframework.stereotype.Component
import ru.revseev.library.domain.Genre
import java.util.*

data class GenreDto(val name: String)

fun GenreDto.toGenre(): Genre = Genre(name = this.name)

fun Collection<GenreDto>.toGenres(): MutableList<Genre> = this.map { it.toGenre() }.toMutableList()

fun Genre.toDto() = GenreDto(this.name)

@Component
class GenreDtoFormatter : Formatter<GenreDto> {

    override fun print(`object`: GenreDto, locale: Locale): String = `object`.name

    override fun parse(text: String, locale: Locale): GenreDto = GenreDto(text)

}