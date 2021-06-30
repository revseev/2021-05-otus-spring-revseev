package ru.revseev.library.dao

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import ru.revseev.library.dao.impl.GenreDaoImpl
import ru.revseev.library.domain.Genre
import ru.revseev.library.exception.DaoException
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.*

@JdbcTest
@Import(GenreDaoImpl::class)
internal class GenreDaoImplTest(@Autowired val dao: GenreDao) {

    @Test
    fun `getAll() should return all expected Genres`() {
        val expected = listOf(Genre(1, "Genre1"), Genre(2, "Genre2"), Genre(3, "Genre3"))
        val actual = dao.getAll()

        expectThat(actual).containsExactlyInAnyOrder(expected)
    }

    @Nested
    inner class GetById {

        @Test
        fun `should return Genre when getting by existing id`() {
            val expected = Genre(1, "Genre1")
            val actual = dao.getById(1)

            expectThat(actual).isEqualTo(expected)
        }

        @Test
        fun `should throw exception when Genre not exist`() {
            val nonExistentId = 4L

            expectThrows<DaoException> { dao.getById(nonExistentId) }
        }
    }

    @Nested
    inner class Add {

        @Test
        fun `should add new Genre, return new id`() {
            val new = Genre(name = "Genre3")
            val newId = dao.add(new)

            expectThat(newId).isGreaterThan(2L)
        }

        @Test
        fun `should not add existing Genre, return existing id`() {
            val existingId = 1L
            val existingGenre = Genre(existingId, "Genre1")
            val newId = dao.add(existingGenre)

            expectThat(newId).isEqualTo(existingId)
        }
    }

    @Nested
    inner class Update {

        @Test
        fun `should update existing Genre`() {
            val existing = Genre(1, "Genre0")
            val isUpdated = dao.update(existing)

            expectThat(isUpdated).isTrue()
        }

        @Test
        fun `should not update non-existing Genre`() {
            val nonExisting = Genre(0, "Genre0")
            val isUpdated = dao.update(nonExisting)

            expectThat(isUpdated).isFalse()
        }
    }

    @Nested
    inner class DeleteById {

        @Test
        fun `should delete existing Genre`() {
            val existingId = 3L
            val isDeleted = dao.deleteById(existingId)

            expectThat(isDeleted).isTrue()
        }

        @Test
        fun `should delete non-existing Genre`() {
            val nonExisingId = 0L
            val isDeleted = dao.deleteById(nonExisingId)

            expectThat(isDeleted).isFalse()
        }
    }
}