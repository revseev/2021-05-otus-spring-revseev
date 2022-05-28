package com.revseev.diversify.visualizer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@EnableCaching
@SpringBootApplication
class DiversifyVisualizerApplication

fun main(args: Array<String>) {
    runApplication<DiversifyVisualizerApplication>(*args)
}
