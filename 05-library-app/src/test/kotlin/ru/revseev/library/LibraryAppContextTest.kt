package ru.revseev.library

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import strikt.api.expectCatching
import strikt.assertions.isSuccess

@SpringBootTest
class LibraryAppContextTest {

    @Test
    fun `should load context without errors`() {
        expectCatching { }.isSuccess()
    }
}
