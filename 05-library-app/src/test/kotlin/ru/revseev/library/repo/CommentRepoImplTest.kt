package ru.revseev.library.repo

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import ru.revseev.library.comment11
import ru.revseev.library.comment12
import ru.revseev.library.domain.Comment
import ru.revseev.library.existingId1
import ru.revseev.library.nonExistingId
import ru.revseev.library.repo.impl.CommentRepoImpl
import strikt.api.expectThat
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.isEqualTo

@DataJpaTest
@Import(CommentRepoImpl::class)
internal class CommentRepoImplTest {

    @Autowired
    lateinit var commentRepo: CommentRepoImpl

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
    }
}