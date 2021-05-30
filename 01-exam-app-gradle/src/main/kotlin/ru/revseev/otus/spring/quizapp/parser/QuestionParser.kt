package ru.revseev.otus.spring.quizapp.parser

import ru.revseev.otus.spring.quizapp.domain.Question

interface QuestionParser {

    fun parse(line: String): Question?
}
