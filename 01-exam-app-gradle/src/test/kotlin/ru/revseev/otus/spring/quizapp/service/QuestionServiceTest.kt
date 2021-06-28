package ru.revseev.otus.spring.quizapp.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.revseev.otus.spring.quizapp.domain.Question
import ru.revseev.otus.spring.quizapp.repo.QuestionRepo
import ru.revseev.otus.spring.quizapp.service.impl.QuestionServiceImpl
import strikt.api.expect
import strikt.assertions.isEqualTo

@ExtendWith(MockKExtension::class)
class QuestionServiceTest {

    @MockK
    lateinit var questionRepo: QuestionRepo

    @MockK
    lateinit var ioProvider: IoProvider

    @MockK
    lateinit var messageProvider: MessageProvider

    @SpyK
    var question = Question("First letter in English alphabet?", listOf("A", "B", "C"))

    @DisplayName("получает список вопросов из репозитория")
    @Test
    fun `should get all questions from repo`() {
        every { questionRepo.getAllQuestions() } returns listOf()

        QuestionServiceImpl(questionRepo, ioProvider, messageProvider).viewAllQuestions()

        verify { questionRepo.getAllQuestions() }
    }

    @Test
    fun `should accept an answer in a number form`() {
        every { questionRepo.getAllQuestions() } returns listOf(question)
        every { ioProvider.readInput() } returns "1"

        QuestionServiceImpl(questionRepo, ioProvider, messageProvider).viewAllQuestions()

        verify(exactly = 1) {
            question.testAnswer(any())
        }
    }

    @Test
    fun `should correctly count failed result`() {
        every { question.testAnswer(any()) } returns false
        every { questionRepo.getAllQuestions() } returns listOf(question)
        every { ioProvider.readInput() } returns "1"

        val failed = QuestionServiceImpl(questionRepo, ioProvider, messageProvider).viewAllQuestions()

        expect {
            that(failed.answeredCorrectly).isEqualTo(0)
            that(failed.totalQuestions).isEqualTo(1)
        }
    }

    @Test
    fun `should correctly count correct result`() {
        every { question.testAnswer(any()) } returns true
        every { questionRepo.getAllQuestions() } returns listOf(question)
        every { ioProvider.readInput() } returns "1"

        val correct = QuestionServiceImpl(questionRepo, ioProvider, messageProvider).viewAllQuestions()

        expect {
            that(correct.answeredCorrectly).isEqualTo(1)
            that(correct.totalQuestions).isEqualTo(1)
        }
    }
}