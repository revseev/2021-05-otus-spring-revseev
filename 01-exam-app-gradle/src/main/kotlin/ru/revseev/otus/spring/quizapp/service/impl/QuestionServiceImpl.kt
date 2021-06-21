package ru.revseev.otus.spring.quizapp.service.impl

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.revseev.otus.spring.quizapp.domain.QuizResult
import ru.revseev.otus.spring.quizapp.repo.QuestionRepo
import ru.revseev.otus.spring.quizapp.service.IoProvider
import ru.revseev.otus.spring.quizapp.service.QuestionService
import kotlin.system.exitProcess

@Service
class QuestionServiceImpl(val questionRepo: QuestionRepo, val ioProvider: IoProvider) : QuestionService {

    override fun viewAllQuestions(): QuizResult {
        val questions = questionRepo.getAllQuestions().shuffled()

        var correctCount = 0
        for (question in questions) {
            val shuffled = question.answerOptions
            val questionText = question.questionText
            val answersFormatted = shuffled
                .mapIndexed { i, it -> "[${i + 1}] $it" }
                .joinToString(separator = "\r\n")

            val questionFormatted = "$questionText\r\n$answersFormatted"
            val answer = interact(questionFormatted, shuffled)

            val isCorrect = question.testAnswer(answer)
            log.debug { "Question: '$questionText', answered correctly: $isCorrect" }
            if (isCorrect) correctCount++
        }
        return QuizResult(correctCount, questions.size)
    }

    private fun interact(questionText: String, answerList: List<String>): String {
        ioProvider.writeOutput(questionText)
        val input = ioProvider.readInput().trim()

        if ("q".equals(input, true)) {
            goodbye()
        }

        return try {
            answerList[input.toInt() - 1]
        } catch (e: Exception) {
            ioProvider.writeOutput("Please, pick an answer and type it's index")//todo message source
            return interact(questionText, answerList)
        }
    }

    private fun goodbye() {
        ioProvider.writeOutput("Goodbye!")//todo message source
        exitProcess(0)
    }
}

private val log = KotlinLogging.logger {}
