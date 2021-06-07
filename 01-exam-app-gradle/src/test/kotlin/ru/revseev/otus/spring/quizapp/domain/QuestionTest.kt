package ru.revseev.otus.spring.quizapp.domain

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.*

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

        expectThat(question.answerOptions).containsExactlyInAnyOrder(options)
    }

    @Test
    fun `should say if a given answer is correct`() {
        val question = Question("First letter in English alphabet?", listOf("A", "B", "C"))

        expectThat(question.testAnswer("A")).isTrue()
        expectThat(question.testAnswer("B")).isFalse()
    }
}