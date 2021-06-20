package ru.revseev.otus.spring.quizapp.service

import ru.revseev.otus.spring.quizapp.domain.UserQuizResult

interface QuizResultViewer {

    fun viewResult(userQuizResult: UserQuizResult)
}