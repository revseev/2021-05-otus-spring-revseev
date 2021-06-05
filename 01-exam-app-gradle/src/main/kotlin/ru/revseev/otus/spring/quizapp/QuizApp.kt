package ru.revseev.otus.spring.quizapp

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import ru.revseev.otus.spring.quizapp.service.QuestionFacade

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
class QuizApp

fun main() {
    val context = AnnotationConfigApplicationContext(QuizApp::class.java)
    val facade = context.getBean(QuestionFacade::class.java)
    facade.runQuiz()
}