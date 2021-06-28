package ru.revseev.otus.spring.quizapp.service

import ru.revseev.otus.spring.quizapp.domain.QuizResult

interface QuestionService {

    fun viewAllQuestions(): QuizResult
}
