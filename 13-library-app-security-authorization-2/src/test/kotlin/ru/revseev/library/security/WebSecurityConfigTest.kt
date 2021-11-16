package ru.revseev.library.security

import io.mockk.clearAllMocks
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpSession
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
        mvc.perform(formLogin("/login").user("user").password("pass"))
            .andExpect(authenticated())
    }

    @Test
    fun `invalid user is not authenticated after login`() {
        mvc.perform(formLogin("/login").user("invalid").password("invalid"))
            .andExpect(unauthenticated())
            .andExpect(status().is3xxRedirection)
    }

    @Test
    fun `admin can access admin-only resource`() {
        val httpSession = mvc.loginAndGetSession("admin", "pass")

        mvc.get("/book/new") { session = httpSession }
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `user can access user-only resource`() {
        val httpSession = mvc.loginAndGetSession("user", "pass")

        mvc.get("/book/all") { session = httpSession }
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `user cannot access admin-only resource`() {
        val httpSession = mvc.loginAndGetSession("user", "pass")

        mvc.get("/book/new") { session = httpSession }
            .andExpect {
                status { isForbidden() }
            }
    }
}

fun MockMvc.loginAndGetSession(login: String, password: String): MockHttpSession =
    perform(
        formLogin("/login").user(login).password(password)
    )
        .andExpect(authenticated())
        .andReturn().request.getSession(false) as MockHttpSession
