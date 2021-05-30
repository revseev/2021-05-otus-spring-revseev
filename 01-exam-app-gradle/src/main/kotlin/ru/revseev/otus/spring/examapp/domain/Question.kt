package ru.revseev.otus.spring.examapp.domain

class Question(
    val questionText: String,
    val answerOptions: MutableList<String> = mutableListOf()
) {
    private var answer: String? = null

    constructor(questionText: String, answerOptions: Iterable<String>) : this(questionText) {
        this.answerOptions.addAll(answerOptions)
    }


}
