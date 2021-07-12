package ru.revseev.library.repo

import org.hibernate.collection.internal.AbstractPersistentCollection
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import ru.revseev.library.*
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Genre
import ru.revseev.library.repo.impl.BookRepoJpa
import ru.revseev.library.repo.impl.GenreRepoJpa
import strikt.api.expectThat
import strikt.assertions.*

@DataJpaTest
@Import(BookRepoJpa::class, GenreRepoJpa::class)
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
            val changed = getFromDb(existingId2).apply { title = "Changed title" }
            val updated = bookRepo.save(changed)

            expectThat(updated).isEqualTo(getFromDb(existingId2))
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
        fun `when delete a book, then comments also deleted via on cascade delete in DB`() {
            val isDeleted = bookRepo.deleteById(existingId1)

            expectThat(isDeleted).isTrue()
            val deletedBookComments = em.entityManager
                .createQuery("SELECT c FROM Comment c where c.book.id = $existingId1")
                .resultList
            expectThat(deletedBookComments).isEmpty()
        }

        @Test
        fun `should not delete non-existing book`() {
            val isDeleted = bookRepo.deleteById(nonExistingId)

            expectThat(isDeleted).isFalse()
        }
    }

    @Nested
    inner class GetBookWithComments {

        @Test
        fun `should return a list with book comments if they exist`() {
            val actual = bookRepo.getBookWithComments(book1)

            expectThat(actual.comments).containsExactlyInAnyOrder(mutableListOf(comment11, comment12))
        }

        @Test
        fun `should return empty list if provided book has no comments`() {
            val bookWithNoComments = Book("Book3", Author("Author3"), mutableListOf(Genre("Genre4")))
            em.persist(bookWithNoComments)
            val actual = bookRepo.getBookWithComments(bookWithNoComments)

            expectThat(actual.comments).containsExactlyInAnyOrder(mutableListOf())
        }
    }

    private fun getFromDb(id: Long) = em.find(Book::class.java, id)
        ?: throw IllegalStateException("Book with $id was expected to exist in persistence layer")
}

