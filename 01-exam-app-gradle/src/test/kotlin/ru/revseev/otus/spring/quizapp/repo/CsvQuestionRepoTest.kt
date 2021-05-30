package ru.revseev.otus.spring.quizapp.repo

import org.junit.jupiter.api.*
import org.mockito.kotlin.*
import ru.revseev.otus.spring.quizapp.domain.Question
import ru.revseev.otus.spring.quizapp.parser.QuestionParser
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
    }

    @BeforeEach
    fun setup() {
        parser = mock {
            on { parse(anyVararg()) } doReturn (Question("Stub"))
        }
    }

    @DisplayName("парсит каждую строку")
    @Test
    fun shouldParseAllLinesFromFileExactly() {
        val questionRepo = CsvQuestionRepo(sourceFileName, parser)
        questionRepo.getAllQuestions()

        verify(parser, times(linesInSourceFile)).parse(any())
    }

    @DisplayName("из валидного файла получает как минимум 1 вопрос")
    @Test
    fun shouldHaveReadSomeQuestionsFromFile() {
        val questionRepo = CsvQuestionRepo(sourceFileName, parser)
        val allQuestions = questionRepo.getAllQuestions()
        expectThat(allQuestions.count()).isGreaterThan(0)
    }
}