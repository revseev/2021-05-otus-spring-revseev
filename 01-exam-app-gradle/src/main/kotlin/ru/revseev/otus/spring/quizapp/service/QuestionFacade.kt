package ru.revseev.otus.spring.quizapp.service

import org.springframework.stereotype.Service

@Service
class QuestionFacade(
        private val questionService: QuestionService,
        private val idService: IdentificationService,
        private val ioProvider: IoProvider
) {

    fun runQuiz() {
        val user = idService.identifyUser()
        val quizResult = questionService.viewAllQuestions()
        quizResult.quizTaker = user
        ioProvider.writeOutput(quizResult.toPrettyString())
    }
}