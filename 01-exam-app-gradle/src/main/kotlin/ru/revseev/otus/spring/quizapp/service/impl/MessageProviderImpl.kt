package ru.revseev.otus.spring.quizapp.service.impl

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import ru.revseev.otus.spring.quizapp.service.MessageProvider

class MessageProviderImpl(private val ms: MessageSource) : MessageProvider {

    override fun getMessage(key: String, vararg args: Any): String {
        return ms.getMessage(key, args, LocaleContextHolder.getLocale())
    }
}