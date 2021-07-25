package ru.revseev.library.repo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.ComponentScan
import ru.revseev.library.book1
import ru.revseev.library.book2
import ru.revseev.library.book3
import strikt.api.expectThat
import strikt.assertions.containsExactlyInAnyOrder

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan("ru.revseev.library.repo")
internal class BookRepoTest {

    @Autowired
    lateinit var bookRepo: BookRepo

    @Test
    fun `getAll() should find all available books`() {
        val expected = listOf(book1, book2, book3)

        val actual = bookRepo.findAll()

        expectThat(actual).containsExactlyInAnyOrder(expected)
    }
}