package ru.revseev.otus.spring.quizapp.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.revseev.otus.spring.quizapp.domain.QuizResult
import ru.revseev.otus.spring.quizapp.domain.User
import ru.revseev.otus.spring.quizapp.domain.UserQuizResult

@ExtendWith(MockKExtension::class)
internal class QuestionFacadeTest {

    @MockK
    lateinit var identificationService: IdentificationService
    @MockK
    lateinit var questionService: QuestionService
    @MockK
    lateinit var quizResultViewer: QuizResultViewer

    @Test
    fun `should identify user, run questions and write output as a result`() {
        val user = User("test", "test")
        every { identificationService.identifyUser() } returns user
        val quizResult = QuizResult(1, 1)
        every { questionService.viewAllQuestions() } returns quizResult

        val facade = QuestionFacade(questionService, identificationService, quizResultViewer)

        facade.runQuiz()

        verifyOrder {
            identificationService.identifyUser()
            questionService.viewAllQuestions()
            quizResultViewer.viewResult(UserQuizResult(user, quizResult))
        }
    }
}