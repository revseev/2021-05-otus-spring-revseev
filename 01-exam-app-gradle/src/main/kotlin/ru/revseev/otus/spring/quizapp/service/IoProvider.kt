package ru.revseev.otus.spring.quizapp.service

interface IoProvider {

    fun writeOutput(output: String)

    fun readInput(): String
}
