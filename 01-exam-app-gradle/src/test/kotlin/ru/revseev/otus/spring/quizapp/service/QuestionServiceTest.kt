package ru.revseev.otus.spring.quizapp.service

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.revseev.otus.spring.quizapp.repo.QuestionRepo

class QuestionServiceTest {

    @DisplayName("получает список вопросов из репозитория")
    @Test
    fun shouldGetAllQuestionsFromRepo() {
        val questionRepo = mock<QuestionRepo>()
        QuestionServiceImpl(questionRepo).viewAllQuestions()

        verify(questionRepo).getAllQuestions()
    }
}