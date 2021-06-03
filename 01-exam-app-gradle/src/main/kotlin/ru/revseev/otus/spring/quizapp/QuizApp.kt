package ru.revseev.otus.spring.quizapp

import org.springframework.context.support.ClassPathXmlApplicationContext
import ru.revseev.otus.spring.quizapp.service.QuestionService

class QuizApp {

    fun main() {
        val context = ClassPathXmlApplicationContext("spring-context.xml")
        val questionService = context.getBean(QuestionService::class.java)
        questionService.viewAllQuestions()
    }
}

fun main() {
    QuizApp().main()
}