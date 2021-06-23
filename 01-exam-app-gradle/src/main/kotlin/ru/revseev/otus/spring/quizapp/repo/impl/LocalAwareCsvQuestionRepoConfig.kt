package ru.revseev.otus.spring.quizapp.repo.impl

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.i18n.LocaleContextHolder

@Configuration
@ConditionalOnProperty(value = ["app.internalization"], havingValue = "true")
class LocalAwareCsvQuestionRepoConfig {

    @Bean
    fun csvQuestionRepo() {
        val locale = LocaleContextHolder.getLocale()

    }
}