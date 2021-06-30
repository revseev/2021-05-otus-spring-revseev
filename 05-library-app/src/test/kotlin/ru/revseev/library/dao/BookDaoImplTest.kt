package ru.revseev.library.dao

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import ru.revseev.library.dao.impl.AuthorDaoImpl
import ru.revseev.library.dao.impl.BookDaoImpl
import ru.revseev.library.dao.impl.GenreDaoImpl
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Genre
import ru.revseev.library.exception.DaoException
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.isTrue

@JdbcTest
@Import(BookDaoImpl::class, AuthorDaoImpl::class, GenreDaoImpl::class)
internal class BookDaoImplTest(@Autowired val dao: BookDao) {

    private val genre1 = Genre(1, "Genre1")
    private val genre2 = Genre(2, "Genre2")
    private val author1 = Author(1, "Author1")
    private val author2 = Author(2, "Author2")
    private val book1 = Book(1, "Book1", author1, mutableListOf(genre1, genre2))
    private val book2 = Book(2, "Book2", author2, mutableListOf(genre2))

    @Test
    fun `getAll() should return all expected Books`() {
        val expected = listOf(book1, book2)
        val actual = dao.getAll()
        println(actual)

        expectThat(actual).containsExactlyInAnyOrder(expected)
    }

    @Nested
    inner class GetById {

        @Test
        fun `should return Book when getting by existing id`() {
            val expected = book1
            println(dao.getAll())
            val actual = dao.getById(1)

            expectThat(actual).isEqualTo(expected)
        }

        @Test
        fun `should throw exception when Book not exist`() {
            val nonExistentId = 3L

            expectThrows<DaoException> { dao.getById(nonExistentId) }
        }
    }

    @Nested
    inner class Add {

        @Test
        fun `should add new Book, return new id`() {
            val newAuthor = Author(name = "Author3")
            val newBook = Book(title = "Book3", author = newAuthor, genres = mutableListOf(genre1))
            val newId = dao.add(newBook)

            expectThat(newId).isGreaterThan(2L)
        }

          @Test
          fun `should not add existing Book, return existing id`() {
              val existing = book1
              val newId = dao.add(existing)

              expectThat(newId).isEqualTo(book1.id)
          }
    }

    @Nested
    inner class Update {

        @Test
        fun `should update existing Book`() {
            val existing = Book(1, "Book0",)
            val isUpdated = dao.update(existing)

            expectThat(isUpdated).isTrue()
        }

        /* @Test
         fun `should not update non-existing Book`() {
             val nonExisting = Book(0, "Book0")
             val isUpdated = dao.update(nonExisting)

             expectThat(isUpdated).isFalse()
         }*/
    }

    @Nested
    inner class DeleteById {

        /*  @Test
          fun `should delete existing Book`() {
              val existingId = 3L
              val isDeleted = dao.deleteById(existingId)

              expectThat(isDeleted).isTrue()
          }

          @Test
          fun `should delete non-existing Book`() {
              val nonExisingId = 0L
              val isDeleted = dao.deleteById(nonExisingId)

              expectThat(isDeleted).isFalse()
          }*/
    }
}

