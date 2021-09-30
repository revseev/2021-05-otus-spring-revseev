package ru.revseev.library.view.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.View
import org.springframework.web.servlet.view.RedirectView

@Controller
class IndexController {

    @GetMapping("/")
    fun index(): View = RedirectView("/book/all")

}