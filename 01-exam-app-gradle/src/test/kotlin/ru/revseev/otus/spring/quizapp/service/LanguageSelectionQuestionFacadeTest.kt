package ru.revseev.otus.spring.quizapp.service

import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import ru.revseev.otus.spring.quizapp.service.impl.LanguageSelectionQuestionFacade

@ExtendWith(MockKExtension::class)
internal class LanguageSelectionQuestionFacadeTest {

    @MockK
    lateinit var localeService: LocaleService

    @MockK
    lateinit var baseQuestionFacade: QuestionFacade

    @Test
    fun `before runQuiz() is ran Locale is set`() {
        val questionFacade = LanguageSelectionQuestionFacade(localeService, baseQuestionFacade)

        questionFacade.runQuiz()

        verifyOrder {
            localeService.setLocale()
            baseQuestionFacade.runQuiz()
        }
    }
}