package ru.revseev.otus.spring.quizapp.parser

import ru.revseev.otus.spring.quizapp.domain.Question

class SimpleQuestionParser : QuestionParser {

    override fun parse(line: String): Question? {
        val splitted = line.split(";").filter { it.isNotBlank() }

        val size = splitted.size
        if (size > 0) {
            val questionText: String = splitted[0]
            val answers = if (size > 1) {
                splitted.subList(1, size)
            } else listOf()
            return Question(questionText, answers)
        }
        return null
    }
}
