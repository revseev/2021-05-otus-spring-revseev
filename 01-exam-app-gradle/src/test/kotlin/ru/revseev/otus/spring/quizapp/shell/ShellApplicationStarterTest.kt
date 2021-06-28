package ru.revseev.otus.spring.quizapp.shell

import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.shell.Shell
import ru.revseev.otus.spring.quizapp.service.QuestionFacade


@SpringBootTest
internal class ShellApplicationStarterTest {

    @Autowired
    lateinit var shell: Shell

    @MockkBean
    lateinit var questionFacade: QuestionFacade

    @Test
    fun `given shell command 'start' should run questionFacade_startQuiz()`() {
        shell.evaluate { "start" }

        verify(exactly = 1) { questionFacade.runQuiz() }
    }
}
