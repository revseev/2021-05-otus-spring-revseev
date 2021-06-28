package ru.revseev.otus.spring.quizapp.shell

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import ru.revseev.otus.spring.quizapp.service.QuestionFacade

@ShellComponent
class ShellApplicationStarter(private val questionFacade: QuestionFacade) {

    @ShellMethod(
        value = "Start quiz application in old-fashioned way, QuestionFacade will handle all user interactions",
        key = ["start"]
    )
    fun runFacade() {
        questionFacade.runQuiz()
    }
}