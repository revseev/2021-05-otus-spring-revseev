package ru.revseev.otus.spring.examapp

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.kotlin.*
import ru.revseev.otus.spring.examapp.domain.Question
import ru.revseev.otus.spring.examapp.parser.QuestionParser
import ru.revseev.otus.spring.examapp.repo.CsvQuestionRepo
import strikt.api.expectThat
import strikt.assertions.isNotEmpty
import strikt.assertions.isNotNull
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuestionRepoTest {

    private val sourceFileName = "questions.csv"
    private var source: Path? = null
    private var linesInSourceFile: Int = 0
    private lateinit var parser: QuestionParser

    @BeforeAll
    fun setupCheck() {
        source = this.javaClass.classLoader.getResource(sourceFileName)?.path?.let(Paths::get)
        expectThat(source).isNotNull()
        linesInSourceFile = Files.readAllLines(source).size
    }

    @BeforeEach
    fun setup() {
        parser = mock {
            on { parse(anyVararg()) } doReturn (Question("Stub"))
        }
    }

    @Test
    fun shouldParseAllLinesFromFileExactly() {
        val questionRepo = CsvQuestionRepo(sourceFileName, parser)
        questionRepo.questions

        verify(parser, times(linesInSourceFile)).parse(any())
    }

    @Test
    fun shouldHaveReadSomeQuestionsFromFile() {
        val questionRepo = CsvQuestionRepo(sourceFileName, parser)
        expectThat(questionRepo.questions).isNotEmpty()
    }
}