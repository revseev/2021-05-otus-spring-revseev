package ru.revseev.otus.spring.quizapp.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.i18n.SimpleLocaleContext
import ru.revseev.otus.spring.quizapp.service.impl.UserInteractiveLocaleService
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo
import java.util.*

@ExtendWith(MockKExtension::class)
@Execution(ExecutionMode.SAME_THREAD)
internal class UserInteractiveLocaleServiceTest {

    @MockK
    lateinit var ioProvider: IoProvider

    @MockK
    lateinit var messageProvider: MessageProvider

    @BeforeEach
    fun setup() {
        LocaleContextHolder.setLocaleContext(SimpleLocaleContext(Locale.getDefault()))
    }

    @Test
    fun `when user doesn't select an option, default locale is used`() {
        every { ioProvider.readInput() } returns ""
        val localeBefore = LocaleContextHolder.getLocale()
        val localeService = UserInteractiveLocaleService(ioProvider, messageProvider)
        val localeAfter = LocaleContextHolder.getLocale()

        localeService.setLocale()
        expectThat(localeBefore).isEqualTo(localeAfter)
    }

    @Test
    fun `when user select an option, locale is changed to selected`() {
        every { ioProvider.readInput() } returns "1"
        val localeBefore = LocaleContextHolder.getLocale()
        val localeService = UserInteractiveLocaleService(ioProvider, messageProvider)

        localeService.setLocale()
        val localeAfter = LocaleContextHolder.getLocale()

        expect {
            that(localeAfter).isNotEqualTo(localeBefore)
            that(localeAfter).isEqualTo(Locale("en"))
        }
    }
}

