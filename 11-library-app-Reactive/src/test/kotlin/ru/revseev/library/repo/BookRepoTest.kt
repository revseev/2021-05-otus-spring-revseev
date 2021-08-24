package ru.revseev.library.repo

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.ComponentScan
import ru.revseev.library.*
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Genre
import strikt.api.expectThat
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.isA

@DataMongoTest
@ComponentScan("ru.revseev.library.repo")
internal class BookRepoTest {

    @Autowired
    lateinit var bookRepo: BookRepo

    @Test
    fun `getAll() should return a List of all available books`() = runBlocking {
        val expected = listOf(book1, book2, book3)

        val actual = bookRepo.findAll().asFlow().toList()

        expectThat(actual) {
            isA<List<Book>>()
            containsExactlyInAnyOrder(expected)
        }
    }

    @Test
    fun `findAllGenres() should return a List of all available genres`() = runBlocking {
        val expected = listOf(genre1, genre2, genre3)

        val actual = bookRepo.findAllGenres().toList()

        expectThat(actual) {
            isA<List<Genre>>()
            containsExactlyInAnyOrder(expected)
        }
    }
}