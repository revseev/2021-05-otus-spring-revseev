package ru.revseev.otus.spring.quizapp.service

import ru.revseev.otus.spring.quizapp.domain.Result

interface QuestionService {

    fun viewAllQuestions(): Result
}
