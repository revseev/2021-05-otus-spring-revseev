package ru.revseev.otus.spring.quizapp.service.impl


import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import ru.revseev.otus.spring.quizapp.service.*

@Service
@Qualifier("languageSelectionQuestionFacade")
class LanguageSelectionQuestionFacade(
    private val localeService: LocaleService,
    @Qualifier("defaultQuestionFacade") private val baseQuestionFacade: QuestionFacade,
) : QuestionFacade {

    override fun runQuiz() {
        localeService.setLocale()
        baseQuestionFacade.runQuiz()
    }
}