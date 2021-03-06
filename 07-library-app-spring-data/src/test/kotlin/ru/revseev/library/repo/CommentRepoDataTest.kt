package ru.revseev.library.repo

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import ru.revseev.library.book2
import ru.revseev.library.comment11
import ru.revseev.library.domain.Comment
import ru.revseev.library.existingId1
import ru.revseev.library.nonExistingId
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.isSuccess

@DataJpaTest
internal class CommentRepoJpaTest {

    @Autowired
    lateinit var commentRepo: CommentRepo

    @Autowired
    lateinit var em: TestEntityManager

    @Nested
    inner class FindById {

        @Test
        fun `should return correct comment by Id`() {
            val expected = comment11

            val actual = commentRepo.findByIdOrNull(existingId1)

            expectThat(actual).isEqualTo(expected)
        }

        @Test
        fun `should return null if nothing found by Id`() {
            val actual = commentRepo.findByIdOrNull(nonExistingId)

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
        fun `should update a comment if it exists in persistence layer`() {
            val changed = getFromDb(existingId1).apply { body = "Changed body" }
            val updated = commentRepo.save(changed)

            expectThat(updated).isEqualTo(getFromDb(existingId1))
        }
    }

    @Nested
    inner class DeleteById {

        @Test
        fun `should delete existing book`() {
            expectCatching { commentRepo.deleteById(existingId1) }.isSuccess()
        }

        @Test
        fun `should not delete non-existing book`() {
            expectThrows<EmptyResultDataAccessException> { commentRepo.deleteById(nonExistingId) }
        }
    }

    private fun getFromDb(id: Long) = em.find(Comment::class.java, id)
        ?: throw IllegalStateException("Comment with $id was expected to exist in persistence layer")
}
