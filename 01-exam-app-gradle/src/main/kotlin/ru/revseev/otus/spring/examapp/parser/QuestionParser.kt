package ru.revseev.otus.spring.examapp.parser

import ru.revseev.otus.spring.examapp.domain.Question

interface QuestionParser {

    fun parse(line: String): Question?
}
