package ru.revseev.otus.spring.quizapp.repo

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.*
import ru.revseev.otus.spring.quizapp.domain.Question
import ru.revseev.otus.spring.quizapp.parser.QuestionParser
import ru.revseev.otus.spring.quizapp.repo.impl.CsvQuestionRepo
import strikt.api.expectThat
import strikt.assertions.isGreaterThan
import strikt.assertions.isNotNull
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CsvQuestionRepoTest {

    private val sourceFileName = "questions.csv"
    private var source: Path? = null
    private var linesInSourceFile: Int = 0
    private lateinit var parser: QuestionParser

    @BeforeAll
    @DisplayName("проверка наличия файла")
    fun setupCheck() {
        source = this.javaClass.classLoader.getResource(sourceFileName)?.path?.let(Paths::get)
        expectThat(source).isNotNull()
        linesInSourceFile = Files.readAllLines(source).size

        MockKAnnotations.init(this)
    }

    @BeforeEach
    fun setup() {
        parser = mockk()
        every { parser.parse(any()) } returns Question("Stub")
    }

    @DisplayName("парсит каждую строку")
    @Test
    fun `should parse all lines from file exactly`() {
        val questionRepo = CsvQuestionRepo(sourceFileName, parser)
        questionRepo.getAllQuestions()

        verify(exactly = linesInSourceFile) {
            parser.parse(any())
        }
    }

    @DisplayName("из валидного файла получает как минимум 1 вопрос")
    @Test
    fun `should have read some questions from file`() {
        val questionRepo = CsvQuestionRepo(sourceFileName, parser)
        val allQuestions = questionRepo.getAllQuestions()
        expectThat(allQuestions.count()).isGreaterThan(0)
    }
}