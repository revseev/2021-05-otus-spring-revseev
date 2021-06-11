package ru.revseev.otus.spring.quizapp.service

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import ru.revseev.otus.spring.quizapp.domain.User
import ru.revseev.otus.spring.quizapp.service.impl.SimpleIdentificationService
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class SimpleIdentificationServiceTest {

    @Test
    fun `given valid input should parse first and second name delimited by comma`() {
        val mockIo = mock<IoProvider> {
            on { readInput() }.thenReturn("Mark, Brown")
        }

        val user = SimpleIdentificationService(mockIo).identifyUser()

        expectThat(user).with(User::name) {
            isEqualTo("Mark")
        }.with(User::lastName) {
            isEqualTo("Brown")
        }
    }

    @Test
    fun `given empty input should ask again`() {
        val mockIo = mock<IoProvider> {
            on { readInput() }.thenReturn("", "J b", "-,,", "Mark, Brown")
        }

        SimpleIdentificationService(mockIo).identifyUser()

        verify(mockIo, times(4)).writeOutput(any())
    }
}
