package ru.revseev.otus.spring.quizapp

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import ru.revseev.otus.spring.quizapp.config.SourceFileProperties
import ru.revseev.otus.spring.quizapp.service.QuestionFacade

@SpringBootApplication
@EnableConfigurationProperties(SourceFileProperties::class)
class QuizApp {

    @Bean
    fun startQuiz(questionFacade: QuestionFacade): ApplicationRunner {
        return ApplicationRunner {
            questionFacade.runQuiz()
        }
    }
}

fun main(args: Array<String>) {
    runApplication<QuizApp>(*args)
}