package ru.revseev.otus.spring.quizapp.domain

class Question(
        val questionText: String,
        val answerOptions: MutableList<String> = mutableListOf()
) {

    constructor(questionText: String, answerOptions: Iterable<String>) : this(questionText) {
        this.answerOptions.addAll(answerOptions)
    }

    fun view(): String {
        val answersFormatted = answerOptions.mapIndexed { i, it -> "[${i + 1}] $it" }.joinToString(separator = "\r\n")
        return "$questionText\r\n$answersFormatted"
    }
}
