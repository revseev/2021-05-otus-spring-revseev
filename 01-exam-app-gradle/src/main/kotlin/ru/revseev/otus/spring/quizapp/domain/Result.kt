package ru.revseev.otus.spring.quizapp.domain

class Result(val answeredCorrectly: Int, val totalQuestions: Int) {

    var quizTaker: User? = null

    override fun toString(): String {
        return "Result(answeredCorrectly=$answeredCorrectly, totalQuestions=$totalQuestions, user=$quizTaker)"
    }

    fun toPrettyString(): String {
        return "${quizTaker?.name} ${quizTaker?.lastName} has answered $answeredCorrectly/$totalQuestions questions correctly!"
    }
}