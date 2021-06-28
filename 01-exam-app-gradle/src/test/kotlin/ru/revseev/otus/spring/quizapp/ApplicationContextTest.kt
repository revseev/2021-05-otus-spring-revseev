package ru.revseev.otus.spring.quizapp

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import strikt.api.expectCatching
import strikt.assertions.isSuccess

@SpringBootTest
internal class ApplicationContextTest {

    @Test
    fun `should load context without errors`() {
        expectCatching { }.isSuccess()
    }
}