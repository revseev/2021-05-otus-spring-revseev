package ru.revseev.otus.spring.quizapp.service

import ru.revseev.otus.spring.quizapp.domain.User

interface IdentificationService {

    fun identifyUser(): User
}
