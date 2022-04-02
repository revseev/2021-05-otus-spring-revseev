package ru.revseev.library.view.controller

import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

private val log = KotlinLogging.logger { }

@Controller
class LoginController {

    @GetMapping("/login")
    fun login(): ModelAndView {
        log.info { "GET: /login" }
        return ModelAndView("login")
    }
}