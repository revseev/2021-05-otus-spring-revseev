package ru.revseev.library.repo

import org.hibernate.collection.internal.AbstractPersistentCollection
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Genre
import ru.revseev.library.repo.impl.BookRepoJpa
import strikt.api.expectThat
import strikt.assertions.*

const val existingId1 = 1L
const val existingId2 = 2L
const val nonExistingId = 0L

val genre1 = Genre("Genre1").apply { id = existingId1 }
val genre2 = Genre("Genre2").apply { id = existingId2 }
val author1 = Author("Author1").apply { id = existingId1 }
val author2 = Author("Author2").apply { id = existingId2 }
val book1 = Book("Book1", author1, mutableListOf(genre1, genre2)).apply { id = existingId1 }
val book2 = Book("Book2", author2, mutableListOf(genre2)).apply { id = existingId2 }

@DataJpaTest
@Import(BookRepoJpa::class)
internal class BookRepoJpaTest {

    @Autowired
    lateinit var bookRepo: BookRepoJpa

    @Autowired
    lateinit var em: TestEntityManager


    @Test
    fun `findAll() should find all books`() {
        val expected = listOf(book1, book2)
        val actual = bookRepo.findAll()

        expectThat(actual).containsExactlyInAnyOrder(expected)
    }

    @Nested
    inner class FindById {

        @Test
        fun `should find existing book by id`() {
            val expected = book1
            val actual: Book? = bookRepo.findById(existingId1)

            expectThat(actual).isEqualTo(expected)
        }

        @Test
        fun `should return null if book does not exist`() {
            val actual: Book? = bookRepo.findById(nonExistingId)

            expectThat(actual).isNull()
        }

        @Test
        fun `should enable lazy-load on genres`() {
            val book = bookRepo.findById(existingId1)!!

            expectThat(book.genres).isA<AbstractPersistentCollection>()
        }
    }

    @Nested
    inner class Save {

        @Test
        fun `should persist a book if it is new`() {
            val newBook = Book("new", Author("new"), mutableListOf(genre2))
            val saved = bookRepo.save(newBook)

            expectThat(saved.isNew()).isFalse()
        }

        @Test
        fun `should update a book if it exists in persistence layer`() {
            val changed = getExistingBook(existingId2).apply { title = "Changed title" }
            val updated = bookRepo.save(changed)

            expectThat(updated).isEqualTo(getExistingBook(existingId2))
        }
    }

    @Nested
    inner class DeleteById {

        @Test
        fun `should delete existing book`() {
            val isDeleted = bookRepo.deleteById(existingId1)

            expectThat(isDeleted).isTrue()
        }

        @Test
        fun `should not delete non-existing book`() {
            val isDeleted = bookRepo.deleteById(nonExistingId)

            expectThat(isDeleted).isFalse()
        }
    }

    private fun getExistingBook(existingId: Long) = em.find(Book::class.java, existingId)
        ?: throw IllegalStateException("Book with $existingId was expected to exist in persistence layer")
}
