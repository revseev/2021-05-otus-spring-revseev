package ru.revseev.library.repo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import ru.revseev.library.domain.Genre
import ru.revseev.library.*
import ru.revseev.library.repo.impl.GenreRepoJpa
import strikt.api.expectThat
import strikt.assertions.containsExactlyInAnyOrder


val genre3 = Genre("Genre3").apply { id = 3L }

@DataJpaTest
@Import(GenreRepoJpa::class)
internal class GenreRepoJpaTest {

    @Autowired
    lateinit var genreRepo: GenreRepoJpa

    @Test
    fun `findAll() should find all Genres`() {
        val expected = listOf(genre1, genre2, genre3)
        val actual = genreRepo.findAll()

        expectThat(actual).containsExactlyInAnyOrder(expected)
    }
}