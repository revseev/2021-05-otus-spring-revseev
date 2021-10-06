package ru.revseev.library.view.dto

import ru.revseev.library.domain.Genre

@JvmInline
value class GenreDto(val name: String)

fun GenreDto.toGenre(): Genre = Genre(name = this.name)

fun Collection<GenreDto>.toGenres(): MutableList<Genre> = this.map { it.toGenre() }.toMutableList()
