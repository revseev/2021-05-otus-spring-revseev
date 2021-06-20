package ru.revseev.otus.spring.quizapp

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import ru.revseev.otus.spring.quizapp.service.QuestionFacade

@SpringBootApplication
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