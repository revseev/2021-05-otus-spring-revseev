package ru.revseev.library.repo

import org.hibernate.collection.internal.AbstractPersistentCollection
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.dao.EmptyResultDataAccessException
import ru.revseev.library.*
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.*

@DataJpaTest
internal class BookRepoJpaTest {

    @Autowired
    lateinit var bookRepo: BookRepo

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
            val actual: Book? = bookRepo.findById(existingId1).orElse(null)

            expectThat(actual).isEqualTo(expected)
        }

        @Test
        fun `should return null if book does not exist`() {
            val actual: Book? = bookRepo.findById(nonExistingId).orElse(null)

            expectThat(actual).isNull()
        }

        @Test
        fun `should enable lazy-load on genres`() {
            val book = bookRepo.findById(existingId1).orElse(null)

            expectThat(book.genres).isA<AbstractPersistentCollection>()
        }
    }

    @Nested
    inner class Save {

        @Test
        fun `should persist a book if it is new`() {
            val newBook = Book("new", Author("new"), mutableListOf())
            val saved = bookRepo.save(newBook)

            expectThat(saved.isNew()).isFalse()
        }

        @Test
        fun `should update a book if it exists in persistence layer`() {
            val changed = getFromDb(existingId2).apply {
                title = "Changed title"
                genres = mutableListOf(genre1)
            }
            val updated = bookRepo.save(changed)

            expectThat(updated).isEqualTo(getFromDb(existingId2))
        }
    }

    @Nested
    inner class DeleteById {

        @Test
        fun `should delete existing book`() {
            expectCatching { bookRepo.deleteById(existingId1) }.isSuccess()
        }

        @Test
        fun `when delete a book, then comments also deleted via on cascade delete in DB`() {
            bookRepo.deleteById(existingId1)

            val deletedBookComments = em.entityManager
                .createQuery("SELECT c FROM Comment c where c.book.id = $existingId1")
                .resultList
            expectThat(deletedBookComments).isEmpty()
        }

        @Test
        fun `should not delete non-existing book`() {
            expectThrows<EmptyResultDataAccessException> { bookRepo.deleteById(nonExistingId) }
        }
    }

    private fun getFromDb(id: Long) = em.find(Book::class.java, id)
        ?: throw IllegalStateException("Book with $id was expected to exist in persistence layer")
}

