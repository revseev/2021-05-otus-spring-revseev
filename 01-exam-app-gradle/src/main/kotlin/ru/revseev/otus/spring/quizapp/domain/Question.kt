package ru.revseev.otus.spring.quizapp.domain

import mu.KotlinLogging

class Question(val questionText: String) {

    var answerOptions: List<String> = listOf()
        get() = field.shuffled() // return shuffled answers every time

    private var correctAnswer: String? = null

    constructor(questionText: String, answerOptions: List<String>) : this(questionText) {
        if (answerOptions.isNotEmpty()) {
            this.answerOptions = answerOptions
            correctAnswer = answerOptions[0] // correctAnswer should be the first provided in source
        }
    }

    fun testAnswer(answer: String): Boolean {
        log.debug { "Testing $answer against $correctAnswer" }
        return answer.equals(correctAnswer, true)
    }
}

private val log = KotlinLogging.logger {}