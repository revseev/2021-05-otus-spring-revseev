package ru.revseev.library.security

import io.mockk.clearAllMocks
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
internal class WebSecurityConfigTest {

    @Autowired
    lateinit var mvc: MockMvc

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @Test
    fun `login page is not protected`() {
        mvc.get("/login")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `protected page redirects to login`() {
        mvc.get("/")
            .andExpect {
                status { is3xxRedirection() }
                redirectedUrlPattern("**/login")
            }
    }

    @Test
    fun `valid user is authenticated after login`() {
        mvc.perform(formLogin("/login").user("user1").password("pass1"))
            .andExpect(authenticated())
    }

    @Test
    fun `invalid user is not authenticated after login`() {
        this.mvc.perform(formLogin("/login").user("invalid").password("invalid"))
            .andExpect(unauthenticated())
            .andExpect(status().is3xxRedirection)
    }
}