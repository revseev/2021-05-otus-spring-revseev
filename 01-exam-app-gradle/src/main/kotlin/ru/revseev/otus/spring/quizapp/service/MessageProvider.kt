package ru.revseev.otus.spring.quizapp.service

interface MessageProvider {

    fun getMessage(key: String, vararg args: Any): String
}