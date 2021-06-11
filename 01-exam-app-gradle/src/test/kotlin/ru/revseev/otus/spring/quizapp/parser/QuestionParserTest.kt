package ru.revseev.otus.spring.quizapp.parser

import org.junit.jupiter.api.Test
import ru.revseev.otus.spring.quizapp.parser.impl.SimpleQuestionParser
import strikt.api.expect
import strikt.assertions.*

class QuestionParserTest {

    private val questionParser = SimpleQuestionParser()

    @Test
    fun shouldParseValidString() {
        val question = questionParser.parse("How are you?;Fine;Not great;Ok")!!

        expect {
            that(question.questionText).isEqualTo("How are you?")
            that(question.answerOptions).containsExactlyInAnyOrder(listOf("Fine", "Not great", "Ok"))
        }
    }

    @Test
    fun shouldReturnNullIfEmptyString() {
        val question = questionParser.parse("")

        expect {
            that(question).isNull()
        }
    }

    @Test
    fun shouldReturnNullIfNoTextBetweenSeparators() {
        val question = questionParser.parse(";;;")

        expect {
            that(question).isNull()
        }
    }

    @Test
    fun shouldReturnOpenQuestionIfNoAnswersGiven() {
        val question = questionParser.parse("What the f*?;;;")!!

        expect {
            that(question.questionText).not { isNullOrBlank() }
            that(question.answerOptions).isEmpty()
        }
    }
}