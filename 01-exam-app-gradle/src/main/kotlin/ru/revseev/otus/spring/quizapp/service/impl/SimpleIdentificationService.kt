package ru.revseev.otus.spring.quizapp.service.impl

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.revseev.otus.spring.quizapp.domain.User
import ru.revseev.otus.spring.quizapp.service.IdentificationService
import ru.revseev.otus.spring.quizapp.service.IoProvider
import java.util.regex.Pattern


@Service
class SimpleIdentificationService(val ioProvider: IoProvider) : IdentificationService {

    override fun identifyUser(): User {
        greeting()
        return identify()
    }

    private fun identify(): User {
        val input = ioProvider.readInput()
        log.debug { "Parsing user input: $input" }

        val trimmedInput = input.trim()
            .split(COMMA_WITH_OR_WITHOUT_SPACES)
            .filter { it.isNotBlank() }
        log.debug { "Trimmed to: $trimmedInput" }

        if (trimmedInput.size > 1) {
            val user = User(trimmedInput[0], trimmedInput[1])
            log.info { "User identified as: $user" }
            return user
        }
        ioProvider.writeOutput("Please, type in your Name and Last Name, comma separated.\n\r(eg. John, Doe)")//todo message source
        return identify()
    }

    private fun greeting() {
        val greeting = """
                |Hello and Welcome to a simple Quiz Application! 
                |Before we start, please, type in your name and last name, comma separated. 
                |(e.i.: John, Doe)""".trimMargin()//todo message source
        ioProvider.writeOutput(greeting)
    }
}

private val log = KotlinLogging.logger {}
private val COMMA_WITH_OR_WITHOUT_SPACES = Pattern.compile(" *+,+ *")