package ru.revseev.otus.spring.examapp

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import ru.revseev.otus.spring.examapp.repo.CsvQuestionRepo
import ru.revseev.otus.spring.examapp.repo.QuestionRepo
import strikt.api.expectThat
import strikt.assertions.isNotNull
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuestionRepoTest {

    private val sourceFileName = "questions.csv"
    private var source: Path? = null

    @BeforeAll
    fun setupCheck() {
        source = this.javaClass.classLoader.getResource(sourceFileName)?.path?.let(Paths::get)
        expectThat(source).isNotNull()
    }

    @Test
    fun should() {

        val questionRepo: QuestionRepo = CsvQuestionRepo(sourceFileName)
        println(questionRepo)
    }
}