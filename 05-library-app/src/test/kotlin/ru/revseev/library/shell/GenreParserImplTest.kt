package ru.revseev.library.shell

import org.junit.jupiter.api.Test
import ru.revseev.library.domain.Genre
import ru.revseev.library.shell.impl.GenreParserImpl
import strikt.api.expectThat
import strikt.assertions.containsExactlyInAnyOrder

internal class GenreParserImplTest {

    private val genreParser = GenreParserImpl()

    @Test
    fun `should correctly parse genres from string input`() {
        val genresString = "genre1, genre2, genre with spaces,genre1"
        val expected = mutableListOf(Genre(name = "genre1"), Genre(name = "genre2"), Genre(name = "genre with spaces"))

        val actual = genreParser.parseGenres(genresString)
        expectThat(actual).containsExactlyInAnyOrder(expected)
    }
}