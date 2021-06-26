package ru.revseev.otus.spring.quizapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.revseev.otus.spring.quizapp.config.SourceFileProperties

@SpringBootApplication
@EnableConfigurationProperties(SourceFileProperties::class)
class QuizApp

fun main(args: Array<String>) {
    runApplication<QuizApp>(*args)
}