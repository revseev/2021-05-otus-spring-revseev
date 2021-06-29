package ru.revseev.library.dao.impl

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import ru.revseev.library.dao.GenreDao
import ru.revseev.library.domain.Genre
import ru.revseev.library.exception.DataNotFoundException
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.isEqualTo

@JdbcTest
@Import(GenreDaoImpl::class)
internal class GenreDaoImplTest(@Autowired val dao: GenreDao) {


    @Test
    fun `getAll() should return all expected genres`() {
        val expected = listOf(Genre(1, "Genre1"), Genre(2, "Genre2"))

        val actual = dao.getAll()

        expectThat(actual).containsExactlyInAnyOrder(expected)
    }

    @Nested
    inner class GetById {

        @Test
        fun `should return genre when getting by existing id`() {
            val expected = Genre(1, "Genre1")

            val actual = dao.getById(1)

            expectThat(actual).isEqualTo(expected)
        }

        @Test
        fun `should throw exception when genre not exist`() {
            val nonExistentId = 3L

            expectThrows<DataNotFoundException> { dao.getById(nonExistentId) }
        }
    }

    @Test
    fun add() {
    }

    @Test
    fun update() {
    }

    @Test
    fun deleteById() {
    }

}