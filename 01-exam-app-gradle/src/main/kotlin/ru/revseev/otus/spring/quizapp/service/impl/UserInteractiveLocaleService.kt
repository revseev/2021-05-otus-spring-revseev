package ru.revseev.otus.spring.quizapp.service.impl

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import ru.revseev.otus.spring.quizapp.service.IoProvider
import ru.revseev.otus.spring.quizapp.service.LocaleService
import ru.revseev.otus.spring.quizapp.service.MessageProvider
import java.util.*

@Service
class UserInteractiveLocaleService(
    private val ioProvider: IoProvider,
    private val messageProvider: MessageProvider
) : LocaleService {

    override fun setLocale() {
        ioProvider.writeOutput(messageProvider.getMessage("language.askUserForLanguage"))

        val input = ioProvider.readInput()

        TODO("determine user input - if ok - change locale, if not - ask again")

    }

    private fun changeLocale() = LocaleContextHolder.setLocale(Locale.ENGLISH)
}