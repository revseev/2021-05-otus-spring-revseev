package ru.revseev.otus.spring.quizapp.service.impl

import mu.KotlinLogging
import net.jcip.annotations.NotThreadSafe
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import ru.revseev.otus.spring.quizapp.service.IoProvider
import ru.revseev.otus.spring.quizapp.service.LocaleService
import ru.revseev.otus.spring.quizapp.service.MessageProvider
import java.util.*

@Service
@NotThreadSafe
class UserInteractiveLocaleService(
    private val ioProvider: IoProvider,
    private val messageProvider: MessageProvider,
) : LocaleService {

    override fun setLocale() {
        val chosenLang = askUserForLanguage()
        if (chosenLang != null) {
            val chosenLocale = Locale(chosenLang)
            log.debug { "User chosen locale: $chosenLocale" }
            LocaleContextHolder.setLocale(chosenLocale)
        }
        log.debug { "Continuing with locale ${LocaleContextHolder.getLocale()}" }
    }

    private fun askUserForLanguage(): String? {
        ioProvider.writeOutput(messageProvider.getMessage("language.askUserForLanguage", System.lineSeparator()))

        val input = ioProvider.readInput().trim()
        log.debug { "User input language: $input" }

        return when (input) {
            "1" -> "en"
            "2" -> "ru"
            "" -> null
            else -> askUserForLanguage()
        }
    }
}

private val log = KotlinLogging.logger {}