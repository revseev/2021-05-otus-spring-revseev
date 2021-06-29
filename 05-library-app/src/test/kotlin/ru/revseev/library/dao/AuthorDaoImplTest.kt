package ru.revseev.library.dao

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import ru.revseev.library.dao.impl.AuthorDaoImpl
import ru.revseev.library.domain.Author
import ru.revseev.library.exception.DaoException
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue

@JdbcTest
@Import(AuthorDaoImpl::class)
internal class AuthorDaoImplTest (@Autowired val dao: AuthorDao){

    @Test
    fun `getAll() should return all expected Genres`() {
        val expected = listOf(Author(1, "Author1"), Author(2, "Author2"), Author(3, "Author3"))
        val actual = dao.getAll()

        expectThat(actual).containsExactlyInAnyOrder(expected)
    }

    @Nested
    inner class GetById {

        @Test
        fun `should return Author when getting by existing id`() {
            val expected = Author(1, "Author1")
            val actual = dao.getById(1)

            expectThat(actual).isEqualTo(expected)
        }

        @Test
        fun `should throw exception when Author not exist`() {
            val nonExistentId = 4L

            expectThrows<DaoException> { dao.getById(nonExistentId) }
        }
    }

    @Nested
    inner class Add {

        @Test
        fun `should add new Author`() {
            val new = Author(name = "Author4")
            val isInserted = dao.add(new)

            expectThat(isInserted).isTrue()
        }

        @Test
        fun `should not add existing Author`() {
            val new = Author(name = "Author1")
            val isInserted = dao.add(new)

            expectThat(isInserted).isFalse()
        }
    }

    @Nested
    inner class Update {

        @Test
        fun `should update existing Author`() {
            val existing = Author(1, "Author0")
            val isUpdated = dao.update(existing)

            expectThat(isUpdated).isTrue()
        }

        @Test
        fun `should not update non-existing Author`() {
            val nonExisting = Author(0, "Author0")
            val isUpdated = dao.update(nonExisting)

            expectThat(isUpdated).isFalse()
        }
    }

    @Nested
    inner class DeleteById {

        @Test
        fun `should delete existing Author`() {
            val existingId = 3L
            val isDeleted = dao.deleteById(existingId)

            expectThat(isDeleted).isTrue()
        }

        @Test
        fun `should delete non-existing Author`() {
            val nonExisingId = 0L
            val isDeleted = dao.deleteById(nonExisingId)

            expectThat(isDeleted).isFalse()
        }
    }
}