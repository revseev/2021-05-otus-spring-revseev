package ru.revseev.library.repo

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import ru.revseev.library.domain.User
import ru.revseev.library.exception.UserAlreadyExistsException
import ru.revseev.library.service.UserService
import ru.revseev.library.service.impl.UserServiceImpl
import ru.revseev.library.admin
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull

@DataMongoTest
@ComponentScan("ru.revseev.library.repo")
@Import(UserServiceImpl::class)
internal class UserServiceTest {

    @Autowired
    lateinit var userService: UserService

    @Nested
    inner class FindByUsername {

        @Test
        fun `should return a User if it exists`() {
            val expected = admin

            val actual = userService.findByUsername(admin.username)

            expectThat(actual) {
                isNotNull()
                isEqualTo(expected)
            }
        }

        @Test
        fun `should return null if user not exists`() {
            val actual = userService.findByUsername("non-existent")

            expectThat(actual) {
                isNull()
            }
        }
    }

    @Nested
    inner class Save {

        @Test
        fun `should save a new user`() {
            val expected = User("new", "")

            val actual = userService.save(expected)

            expectThat(actual) {
                isEqualTo(expected)
            }
        }

        @Test
        fun `should throw a specific exception if saving user with non-unique username`() {
            val withSameUsername = User("user", "new")

            expectThrows<UserAlreadyExistsException> {
                userService.save(withSameUsername)
            }
        }
    }
}