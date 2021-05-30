package ru.revseev.otus.spring.examapp.repo

import ru.revseev.otus.spring.examapp.domain.Question
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class CsvQuestionRepo(var source: String) : QuestionRepo {

    private val questionLines: List<String>
    val questions: List<Question>

    init {
        questionLines = readSourceFile()
        questions = questionLines.mapNotNull { parseQuestion(it) }
    }

    private fun readSourceFile(): List<String> {
        val resource = this.javaClass.classLoader.getResource(source).also(::println)
        return resource?.let {
            val path = Paths.get(it.path)
            return Files.readAllLines(path, StandardCharsets.UTF_8)
        } ?: listOf()
    }

    private fun parseQuestion(line: String): Question? {
        return if (line.isNotBlank()) {
            val splitted = line.split(";").filter { it.isNotBlank() }

            val size = splitted.size
            if (size > 0) {
                val questionText: String = splitted[0]
                val answers = if (size > 1) {
                    splitted.subList(1, size)
                } else listOf()
                return Question(questionText, answers)
            } else null
        } else null
    }
}
