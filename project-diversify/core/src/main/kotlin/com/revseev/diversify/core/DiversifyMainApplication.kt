package com.revseev.diversify.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DiversifyMainApplication

fun main(args: Array<String>) {
    runApplication<DiversifyMainApplication>(*args)
}
