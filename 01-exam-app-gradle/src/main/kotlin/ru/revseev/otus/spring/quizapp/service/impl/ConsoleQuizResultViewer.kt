package ru.revseev.otus.spring.quizapp.service.impl

import org.springframework.stereotype.Service
import ru.revseev.otus.spring.quizapp.domain.UserQuizResult
import ru.revseev.otus.spring.quizapp.service.IoProvider
import ru.revseev.otus.spring.quizapp.service.MessageProvider
import ru.revseev.otus.spring.quizapp.service.QuizResultViewer

@Service
class ConsoleQuizResultViewer(
    private val ioProvider: IoProvider,
    private val messageProvider: MessageProvider
) : QuizResultViewer {

    override fun viewResult(userQuizResult: UserQuizResult) {
        val quizTaker = userQuizResult.quizTaker
        val result = userQuizResult.result
        val resultMessage = messageProvider.getMessage(
            "result.viewUserCorrectAnswersRatio",
            quizTaker.name,
            quizTaker.lastName,
            result.answeredCorrectly,
            result.totalQuestions
        )
        ioProvider.writeOutput(resultMessage)
    }
}