package ru.revseev.otus.spring.examapp.repo

import ru.revseev.otus.spring.examapp.domain.Question
import ru.revseev.otus.spring.examapp.parser.QuestionParser
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class CsvQuestionRepo(val csvSource: String, val questionParser: QuestionParser) : QuestionRepo {

    private val questionLines: List<String>
    val questions: List<Question>

    init {
        questionLines = readSourceFile()
        questions =  questionLines.mapNotNull { questionParser.parse(it) }
    }

    private fun readSourceFile(): List<String> {
        val resource = this.javaClass.classLoader.getResource(csvSource).also(::println)
        return resource?.let {
            val path = Paths.get(it.path)
            return Files.readAllLines(path, StandardCharsets.UTF_8)
        } ?: listOf()
    }
}
