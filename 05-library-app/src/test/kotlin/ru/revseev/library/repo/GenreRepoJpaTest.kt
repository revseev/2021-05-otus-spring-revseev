package ru.revseev.library.repo

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import ru.revseev.library.domain.Genre
import ru.revseev.library.genre1
import ru.revseev.library.genre2
import ru.revseev.library.repo.impl.GenreRepoJpa
import strikt.api.expectThat
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.isEqualTo
import strikt.assertions.isNull


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

    @Nested
    inner class FindByName {

        @Test
        fun `findByName() should find 1 existing genre by name`() {
            val existingName = genre3.name
            val actual = genreRepo.findByName(existingName)

            expectThat(actual).isEqualTo(genre3)
        }

        @Test
        fun `findByName() should return null if nothing is found`() {
            val nonExistingName = "no"
            val actual = genreRepo.findByName(nonExistingName)

            expectThat(actual).isNull()
        }
    }
    @Nested
    inner class Save {

        @Test
        fun `should return genre if it exists by name`() {
            TODO()
        }
        @Test
        fun `should save genre if it is new`() {
            TODO()
        }
        @Test
        fun `should merge genre if it is not new`() {
            TODO()
        }
    }
}