package ru.revseev.otus.spring.quizapp.service

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import ru.revseev.otus.spring.quizapp.domain.Question
import ru.revseev.otus.spring.quizapp.repo.QuestionRepo
import ru.revseev.otus.spring.quizapp.service.impl.QuestionServiceImpl
import strikt.api.expect
import strikt.assertions.isEqualTo

class QuestionServiceTest {

    private val question = Question("First letter in English alphabet?", listOf("A", "B", "C"))

    @DisplayName("получает список вопросов из репозитория")
    @Test
    fun shouldGetAllQuestionsFromRepo() {
        val mockQuestionRepo = mock<QuestionRepo>()
        val mockIo = mock<IoProvider>()

        QuestionServiceImpl(mockQuestionRepo, mockIo).viewAllQuestions()

        verify(mockQuestionRepo).getAllQuestions()
    }

    @Test
    fun `should accept an answer in a number form`() {
        val spyQuestion = spy(question)
        val mockQuestionRepo = mock<QuestionRepo> {
            on { getAllQuestions() }.doReturn(listOf(spyQuestion))
        }
        val mockIo = mock<IoProvider> {
            on { readInput() }.doReturn("1")
        }

        QuestionServiceImpl(mockQuestionRepo, mockIo).viewAllQuestions()

        verify(spyQuestion, times(1)).testAnswer(any())
    }

    @Test
    fun `should correctly count failed result`() {
        val alwaysFailQuestion = spy(question) {
            onGeneric { testAnswer(any()) }.doReturn(false)
        }
        val mockQuestionRepo = mock<QuestionRepo> {
            on { getAllQuestions() }.doReturn(listOf(alwaysFailQuestion))
        }
        val mockIo = mock<IoProvider> {
            on { readInput() }.doReturn("1")
        }

        val failed = QuestionServiceImpl(mockQuestionRepo, mockIo).viewAllQuestions()

        expect {
            that(failed.answeredCorrectly).isEqualTo(0)
            that(failed.totalQuestions).isEqualTo(1)
        }
    }

    @Test
    fun `should correctly count correct result`() {
        val alwaysCorrectQuestion = spy(question) {
            onGeneric { testAnswer(any()) }.doReturn(true)
        }
        val mockQuestionRepo = mock<QuestionRepo> {
            on { getAllQuestions() }.doReturn(listOf(alwaysCorrectQuestion))
        }
        val mockIo = mock<IoProvider> {
            on { readInput() }.doReturn("1")
        }

        val correct = QuestionServiceImpl(mockQuestionRepo, mockIo).viewAllQuestions()

        expect {
            that(correct.answeredCorrectly).isEqualTo(1)
            that(correct.totalQuestions).isEqualTo(1)
        }
    }
}