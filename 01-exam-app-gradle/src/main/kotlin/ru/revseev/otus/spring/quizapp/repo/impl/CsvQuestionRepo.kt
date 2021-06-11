package ru.revseev.otus.spring.quizapp.repo.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import ru.revseev.otus.spring.quizapp.domain.Question
import ru.revseev.otus.spring.quizapp.parser.QuestionParser
import ru.revseev.otus.spring.quizapp.repo.QuestionRepo
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets

@Repository
class CsvQuestionRepo(
    @Value("\${app.source-file.name}") val csvSource: String,
    val questionParser: QuestionParser
) : QuestionRepo {

    private val questionLines: List<String> = readSourceFile()

    private fun readSourceFile(): List<String> {
        return this.javaClass.classLoader.getResourceAsStream(csvSource)
            ?.bufferedReader(StandardCharsets.UTF_8)
            ?.readLines()
            ?: throw FileNotFoundException("Source file '$csvSource' is not found!")
    }

    override fun getAllQuestions(): Iterable<Question> {
        return questionLines.mapNotNull { questionParser.parse(it) }
    }
}
