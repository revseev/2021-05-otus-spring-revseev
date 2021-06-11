package ru.revseev.otus.spring.quizapp.service

import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.revseev.otus.spring.quizapp.domain.UserQuizResult
import ru.revseev.otus.spring.quizapp.service.impl.ConsoleQuizResultViewer

@ExtendWith(MockKExtension::class)
internal class ConsoleQuizResultViewerTest {

    @MockK
    lateinit var ioProvider: IoProvider

    @MockK
    lateinit var userQuizResult: UserQuizResult

    @Test
    fun `should provide some output to IoProvider`() {
        ConsoleQuizResultViewer(ioProvider).viewResult(userQuizResult)

        verify(exactly = 1) {
            ioProvider.writeOutput(any())
        }
    }
}