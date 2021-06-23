package ru.revseev.otus.spring.quizapp.service.impl

import org.springframework.context.MessageSource
import ru.revseev.otus.spring.quizapp.service.MessageProvider
import java.util.*

class DefaultLocaleMessageProvider(private val ms: MessageSource) : MessageProvider {

    private val defaultLocale = Locale("en")

    override fun getMessage(key: String, vararg args: Any) = ms.getMessage(key, args, defaultLocale)
}