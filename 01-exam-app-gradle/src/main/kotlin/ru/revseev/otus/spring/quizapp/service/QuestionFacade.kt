package ru.revseev.otus.spring.quizapp.service

import org.springframework.stereotype.Service
import ru.revseev.otus.spring.quizapp.domain.UserQuizResult

@Service
class QuestionFacade(
    private val localeService: LocaleService,
    private val questionService: QuestionService,
    private val idService: IdentificationService,
    private val quizResultViewer: QuizResultViewer,
) {

    fun runQuiz() {

        val user = idService.identifyUser()
        val quizResult = questionService.viewAllQuestions()
        quizResultViewer.viewResult(UserQuizResult(user, quizResult))
    }
}