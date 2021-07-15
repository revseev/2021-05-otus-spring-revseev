package ru.revseev.library.repo

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import ru.revseev.library.domain.Genre
import ru.revseev.library.existingId1
import ru.revseev.library.genre1
import ru.revseev.library.genre2
import ru.revseev.library.genre3
import ru.revseev.library.repo.impl.GenreRepoJpa
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.*


@DataJpaTest
@Import(GenreRepoJpa::class)
internal class GenreRepoJpaTest {

    @Autowired
    lateinit var genreRepo: GenreRepoJpa
    @Autowired
    lateinit var em: TestEntityManager

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
        fun `should return existing genre if it exists by name`() {
            val existing = Genre("Genre1")
            val persisted = genreRepo.save(existing)

            expectThat(persisted.id).isEqualTo(existingId1)
        }

        @Test
        fun `should save genre if it is new`() {
            val new = Genre("new")
            val persisted = genreRepo.save(new)

            expect {
                that(persisted.id).isNotNull().and { isGreaterThan(3L) }
                that(persisted.name).isEqualTo(new.name)
            }
        }
    }

    @Nested
    inner class SaveAll {

        private val existing = Genre("Genre1")
        private val new = Genre("new")
        private val modified = Genre("Modified").apply { id = 2L }
        private val genresToSave = listOf(existing, new, modified)

        @Test
        fun `should save genres if it is new`() {
            val persistedGenres = genreRepo.saveAll(genresToSave)

            expect {
                that(persistedGenres).hasSize(3)
                val actual = persistedGenres.find { it.name == "new" }
                that(actual).isNotNull().get { id }.isNotNull().and { isGreaterThan(3L) }
            }
        }

        @Test
        fun `should save existing genre if it's name exist`() {
            val persistedGenres = genreRepo.saveAll(genresToSave)

            expect {
                that(persistedGenres).hasSize(3)
                that(persistedGenres).contains(modified)
                that(getFromDb(2L)).isEqualTo(modified)
            }
        }

        @Test
        fun `should update name of a genre if it's name exist`() {
            val persistedGenres = genreRepo.saveAll(genresToSave)

            expect {
                that(persistedGenres).hasSize(3)
                that(persistedGenres).contains(genre1)
                that(getFromDb(1L)).isEqualTo(genre1)
            }
        }

    }

    private fun getFromDb(id: Long): Genre = em.find(Genre::class.java, id)
        ?: throw IllegalStateException("Genre with $id was expected to exist in persistence layer")
}