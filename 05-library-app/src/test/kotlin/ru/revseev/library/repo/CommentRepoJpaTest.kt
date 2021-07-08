package ru.revseev.library.repo

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import ru.revseev.library.*
import ru.revseev.library.domain.Comment
import ru.revseev.library.repo.impl.CommentRepoJpa
import strikt.api.expectThat
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull

@DataJpaTest
@Import(CommentRepoJpa::class)
internal class CommentRepoJpaTest {

    @Autowired
    lateinit var commentRepo: CommentRepoJpa

    @Autowired
    lateinit var em: TestEntityManager

    @Nested
    inner class FindByBookId {

        @Test
        fun `should return correctly comment by book id`() {
            val expected = mutableListOf(comment11, comment12)

            val actual = commentRepo.findByBookId(1L)

            expectThat(actual).containsExactlyInAnyOrder(expected)
        }

        @Test
        fun `should return empty list if non-existing book id`() {
            val expected = mutableListOf<Comment>()

            val actual = commentRepo.findByBookId(nonExistingId)

            expectThat(actual).isEqualTo(expected)
        }
    }

    @Nested
    inner class FindById {

        @Test
        fun `should return correct comment by Id`() {
            val expected = comment11

            val actual = commentRepo.findById(existingId1)

            expectThat(actual).isEqualTo(expected)
        }

        @Test
        fun `should return null if nothing found by Id`() {
            val actual = commentRepo.findById(nonExistingId)

            expectThat(actual).isNull()
        }
    }

    @Nested
    inner class Save {
        @Test
        fun `should persist a comment`() {
            val new = Comment(book2, "body")

            val newId = commentRepo.save(new).id

            expectThat(newId).isNotNull()

            val persisted = getFromDb(newId!!)
            expectThat(persisted) {
                get { body }.isEqualTo("body")
                get { book }.isEqualTo(book2)
            }
        }

        @Test
        fun merge() {
            TODO("Impl")
        }
    }

    @Test
    fun delete() {
        TODO("Impl")
    }

    private fun getFromDb(id: Long) = em.find(Comment::class.java, id)
        ?: throw IllegalStateException("Comment with $id was expected to exist in persistence layer")
}