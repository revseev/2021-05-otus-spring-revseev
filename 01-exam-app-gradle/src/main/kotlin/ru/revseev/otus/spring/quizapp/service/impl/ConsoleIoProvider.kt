package ru.revseev.otus.spring.quizapp.service.impl

import org.springframework.stereotype.Service
import ru.revseev.otus.spring.quizapp.service.IoProvider
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.util.*

@Service
class ConsoleIoProvider(
    inputStream: InputStream = System.`in`,
    outputStream: OutputStream = System.out
) : IoProvider {

    private val scanner: Scanner = Scanner(inputStream)
    private val printStream: PrintStream = PrintStream(outputStream)

    override fun writeOutput(output: String) {
        printStream.println(output)
    }

    override fun readInput(): String {
        return scanner.nextLine() ?: throw IllegalArgumentException("User input couldn't be null")
    }
}