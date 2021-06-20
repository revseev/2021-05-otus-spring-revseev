package ru.revseev.otus.spring.quizapp.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app.source-filename")
data class SourceFileProperties(
    val default: String,
    val languages: Map<String, String>
)