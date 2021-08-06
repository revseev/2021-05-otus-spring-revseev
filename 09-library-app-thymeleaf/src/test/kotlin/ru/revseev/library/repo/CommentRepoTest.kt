package ru.revseev.library.repo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.ComponentScan
import ru.revseev.library.comment21
import ru.revseev.library.comment22
import ru.revseev.library.domain.Comment
import strikt.api.expectThat
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.isA

@DataMongoTest
@ComponentScan("ru.revseev.library.repo")
internal class CommentRepoTest {

    @Autowired
    lateinit var commentRepo: CommentRepo

    @Test
    fun `findAllById() should return a MutableList with comments by provided ids`() {
        val expected = mutableListOf(comment21, comment22)

        val actual = commentRepo.findAllById(mutableListOf(comment21.id, comment22.id))

        expectThat(actual) {
            isA<MutableList<Comment>>()
            containsExactlyInAnyOrder(expected)
        }
    }
}
