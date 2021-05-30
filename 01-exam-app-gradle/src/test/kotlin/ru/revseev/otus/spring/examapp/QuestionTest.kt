package ru.revseev.otus.spring.examapp

import strikt.api.*
import strikt.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.revseev.otus.spring.examapp.domain.Question

@DisplayName("Класс Question")
class QuestionTest {

    @DisplayName("дожен вернуть текст вопроса")
    @Test
    fun shouldGiveQuestionText() {
        val questionText = "How are you?"
        val question = Question(questionText)
        expectThat(question.questionText).isA<String>().and { isEqualTo(questionText) }
    }

    @DisplayName("должен содержать предоставленные варианты ответа")
    @Test
    fun shouldContainProvidedOptions() {
        val options = listOf("24", "42", "I'm not sure")
        val question = Question("Answer to the Ultimate Question of Life, the Universe, and Everything?", options)
        expectThat(question.answerOptions).containsExactly(options)
    }
}