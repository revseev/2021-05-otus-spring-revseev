package ru.revseev.otus.spring.quizapp.service

import ru.revseev.otus.spring.quizapp.domain.Question
import ru.revseev.otus.spring.quizapp.repo.QuestionRepo
import java.util.*

class QuestionServiceImpl(val questionRepo: QuestionRepo) : QuestionService {

    override fun viewAllQuestions() {
        val questions = questionRepo.getAllQuestions().map(Question::view)
        val scanner = Scanner(System.`in`)
        for (question in questions) {
            println(question)
            val input = scanner.nextLine().trim()
            if ("q".equals(input, true)) {
                return
            }
        }
    }
}
