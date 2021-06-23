package ru.revseev.otus.spring.quizapp.service.impl


import ru.revseev.otus.spring.quizapp.service.LocaleService
import ru.revseev.otus.spring.quizapp.service.QuestionFacade

class LanguageSelectionQuestionFacade(
    private val localeService: LocaleService,
    private val baseQuestionFacade: QuestionFacade,
) : QuestionFacade {

    override fun runQuiz() {
        localeService.setLocale()
        baseQuestionFacade.runQuiz()
    }
}