package ru.revseev.otus.spring.quizapp.service.impl

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import ru.revseev.otus.spring.quizapp.domain.UserQuizResult
import ru.revseev.otus.spring.quizapp.service.IdentificationService
import ru.revseev.otus.spring.quizapp.service.QuestionFacade
import ru.revseev.otus.spring.quizapp.service.QuestionService
import ru.revseev.otus.spring.quizapp.service.QuizResultViewer

@Service
@Qualifier("defaultQuestionFacade")
class DefaultQuestionFacade(
    private val questionService: QuestionService,
    private val idService: IdentificationService,
    private val quizResultViewer: QuizResultViewer,
): QuestionFacade {

    override fun runQuiz() {
        val user = idService.identifyUser()
        val quizResult = questionService.viewAllQuestions()
        quizResultViewer.viewResult(UserQuizResult(user, quizResult))
    }
}