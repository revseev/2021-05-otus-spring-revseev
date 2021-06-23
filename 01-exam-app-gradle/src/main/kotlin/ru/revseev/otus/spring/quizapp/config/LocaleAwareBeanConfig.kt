package ru.revseev.otus.spring.quizapp.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Primary
import org.springframework.context.i18n.LocaleContextHolder
import ru.revseev.otus.spring.quizapp.parser.QuestionParser
import ru.revseev.otus.spring.quizapp.repo.QuestionRepo
import ru.revseev.otus.spring.quizapp.repo.impl.CsvQuestionRepo
import ru.revseev.otus.spring.quizapp.service.LocaleService
import ru.revseev.otus.spring.quizapp.service.MessageProvider
import ru.revseev.otus.spring.quizapp.service.QuestionFacade
import ru.revseev.otus.spring.quizapp.service.impl.DefaultLocaleMessageProvider
import ru.revseev.otus.spring.quizapp.service.impl.LanguageSelectionQuestionFacade
import ru.revseev.otus.spring.quizapp.service.impl.MessageProviderImpl

@Configuration
class LocaleAwareBeanConfig {

    @Bean
    @Lazy
    @ConditionalOnProperty(value = ["app.use-internalization"], havingValue = "true")
    fun localeAwareCsvQuestionRepo(
        sourceFileProps: SourceFileProperties,
        questionParser: QuestionParser
    ): QuestionRepo {
        val lang = LocaleContextHolder.getLocale().language
        val filename: String = sourceFileProps.languages[lang] ?: sourceFileProps.default
        return CsvQuestionRepo(filename, questionParser)
    }

    @Bean
    @ConditionalOnProperty(value = ["app.use-internalization"], havingValue = "false", matchIfMissing = true)
    fun csvQuestionRepo(sourceFileProps: SourceFileProperties, questionParser: QuestionParser): QuestionRepo =
        CsvQuestionRepo(sourceFileProps.default, questionParser)


    @Bean
    @ConditionalOnProperty(value = ["app.use-internalization"], havingValue = "true")
    fun messageProviderImpl(ms: MessageSource): MessageProvider = MessageProviderImpl(ms)

    @Bean
    @ConditionalOnProperty(value = ["app.use-internalization"], havingValue = "false", matchIfMissing = true)
    fun defaultLocaleMessageProvider(ms: MessageSource): MessageProvider = DefaultLocaleMessageProvider(ms)

    @Bean
    @Primary
    @ConditionalOnProperty(value = ["app.use-internalization"], havingValue = "true", matchIfMissing = false)
    fun languageSelectionQuestionFacade(
        localeService: LocaleService,
        baseQuestionFacade: QuestionFacade,
    ): QuestionFacade = LanguageSelectionQuestionFacade(localeService, baseQuestionFacade)
    
}