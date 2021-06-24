package ru.revseev.otus.spring.quizapp.service

import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.MessageSource
import ru.revseev.otus.spring.quizapp.service.impl.MessageProviderImpl

@ExtendWith(MockKExtension::class)
internal class MessageProviderImplTest {

    @MockK
    lateinit var ms: MessageSource

    @Test
    fun `when getMessage() then message key and args are delegated to MessageSource`() {
        val messageProvider = MessageProviderImpl(ms)
        val key = "someKey"
        val someArgs = arrayOf(1, 2, 3)

        messageProvider.getMessage(key, *someArgs)

        verify(exactly = 1) {
            ms.getMessage(key, someArgs, any())
        }
    }
}