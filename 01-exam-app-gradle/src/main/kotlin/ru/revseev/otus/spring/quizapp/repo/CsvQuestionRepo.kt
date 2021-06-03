package ru.revseev.otus.spring.quizapp.repo

import ru.revseev.otus.spring.quizapp.domain.Question
import ru.revseev.otus.spring.quizapp.parser.QuestionParser
import java.nio.charset.StandardCharsets

class CsvQuestionRepo(val csvSource: String, val questionParser: QuestionParser) : QuestionRepo {

    private val questionLines: List<String> = readSourceFile()

    private fun readSourceFile(): List<String> {
        return this.javaClass.classLoader.getResourceAsStream(csvSource)
                ?.bufferedReader(StandardCharsets.UTF_8)
                ?.readLines() ?: listOf()

    }

    override fun getAllQuestions(): Iterable<Question> {
        return questionLines.mapNotNull { questionParser.parse(it) }
    }

}
