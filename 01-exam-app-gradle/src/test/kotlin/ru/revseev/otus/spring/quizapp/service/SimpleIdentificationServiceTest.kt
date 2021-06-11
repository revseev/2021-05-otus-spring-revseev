package ru.revseev.otus.spring.quizapp.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.revseev.otus.spring.quizapp.domain.User
import ru.revseev.otus.spring.quizapp.service.impl.SimpleIdentificationService
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@ExtendWith(MockKExtension::class)
class SimpleIdentificationServiceTest {

    @MockK
    lateinit var mockIo: IoProvider

    @Test
    fun `given valid input should parse first and second name delimited by comma`() {
        every { mockIo.readInput() } returns ("Mark, Brown")

        val user = SimpleIdentificationService(mockIo).identifyUser()

        expectThat(user).with(User::name) {
            isEqualTo("Mark")
        }.with(User::lastName) {
            isEqualTo("Brown")
        }
    }

    @Test
    fun `given empty input should ask again`() {
        every { mockIo.readInput() }.returnsMany("", "J b", "-,,", "Mark, Brown")

        SimpleIdentificationService(mockIo).identifyUser()

        verify(exactly = 4) {
            mockIo.writeOutput(any())
        }
    }
}
