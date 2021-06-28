package ru.revseev.otus.spring.quizapp.service

import org.junit.jupiter.api.Test
import ru.revseev.otus.spring.quizapp.service.impl.ConsoleIoProvider
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

internal class ConsoleIoProviderTest {

    @Test
    fun `should read exactly what's been provided to input stream`() {
        val expected = "test input"
        val inputStream: InputStream = expected.byteInputStream()
        val ioProvider = ConsoleIoProvider(inputStream, ByteArrayOutputStream())

        val actual: String = ioProvider.readInput()

        expectThat(expected).isEqualTo(actual)
    }

    @Test
    fun `should write to output stream a exactly what's been provided and a line separator`() {
        val expected = "test output"
        val outputStream = ByteArrayOutputStream()
        val ioProvider = ConsoleIoProvider(ByteArrayInputStream(byteArrayOf()), outputStream)

        ioProvider.writeOutput(expected)
        val actual = outputStream.toString(Charsets.UTF_8)

        expectThat(expected + System.lineSeparator()).isEqualTo(actual)
    }
}