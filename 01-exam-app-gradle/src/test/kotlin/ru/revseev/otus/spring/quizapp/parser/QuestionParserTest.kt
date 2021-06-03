package ru.revseev.otus.spring.quizapp.parser

import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import strikt.assertions.isNullOrBlank

class QuestionParserTest {

    private val questionParser = SimpleQuestionParser()

    @Test
    fun shouldParseValidString() {
        val qLine = "How are you?;Fine;Not great;Ok"
        val question = questionParser.parse(qLine)
        expect {
            that(question?.questionText).isEqualTo("How are you?")
            that(question?.answerOptions).isEqualTo(mutableListOf("Fine", "Not great", "Ok"))
        }
    }

    @Test
    fun shouldReturnNullIfEmptyString() {
        val qLine = ""
        val question = questionParser.parse(qLine)
        expect {
            that(question).isNull()
        }
    }

    @Test
    fun shouldReturnNullIfNoTextBetweenSeparators() {
        val qLine = ";;;"
        val question = questionParser.parse(qLine)
        expect {
            that(question).isNull()
        }
    }

    @Test
    fun shouldReturnOpenQuestionIfNoAnswersGiven() {
        val qLine = "What the f*?;;;"
        val question = questionParser.parse(qLine)
        expect {
            that(question?.questionText).not { isNullOrBlank() }
            that(question?.answerOptions).isEqualTo(mutableListOf())
        }
    }
}