package ru.revseev.otus.spring.quizapp.service.impl

import org.springframework.stereotype.Service
import ru.revseev.otus.spring.quizapp.domain.UserQuizResult
import ru.revseev.otus.spring.quizapp.service.IoProvider
import ru.revseev.otus.spring.quizapp.service.QuizResultViewer

@Service
class ConsoleQuizResultViewer(private val ioProvider: IoProvider) : QuizResultViewer {

    override fun viewResult(userQuizResult: UserQuizResult) {
        val quizTaker = userQuizResult.quizTaker
        val result = userQuizResult.result
        val resultMessage = "${quizTaker.name} ${quizTaker.lastName} has answered " +
                "${result.answeredCorrectly}/${result.totalQuestions} questions correctly!"
        //todo message source
        ioProvider.writeOutput(resultMessage)
    }
}