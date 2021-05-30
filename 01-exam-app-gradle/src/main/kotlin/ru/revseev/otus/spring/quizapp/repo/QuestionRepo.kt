package ru.revseev.otus.spring.quizapp.repo

import ru.revseev.otus.spring.quizapp.domain.Question

interface QuestionRepo {

    fun getAllQuestions(): Iterable<Question>
}
