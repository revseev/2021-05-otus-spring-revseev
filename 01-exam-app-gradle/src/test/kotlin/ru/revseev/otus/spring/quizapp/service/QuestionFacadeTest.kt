package ru.revseev.otus.spring.quizapp.service

import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import ru.revseev.otus.spring.quizapp.domain.Result
import ru.revseev.otus.spring.quizapp.domain.User

internal class QuestionFacadeTest {

    @Test
    fun `should identify user, run questions and write output as a result`() {
        val identificationServiceMock = mock<IdentificationService> {
            on { identifyUser() }.doReturn(User("test", "test"))
        }
        val questionServiceMock = mock<QuestionService> {
            on { viewAllQuestions() }.doReturn(Result(1, 1))
        }
        val ioProviderMock = mock<IoProvider>()
        val facade = QuestionFacade(questionServiceMock, identificationServiceMock, ioProviderMock)

        facade.runQuiz()

        verify(identificationServiceMock, times(1)).identifyUser()
        verify(questionServiceMock, times(1)).viewAllQuestions()
        verify(ioProviderMock, times(1)).writeOutput(any())
    }
}