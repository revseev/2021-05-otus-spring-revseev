package ru.revseev.otus.spring.quizapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Component

//@Component
@ConstructorBinding
@ConfigurationProperties(prefix = "app.source-filename")
data class SourceFileConfig(
    val default: String,
    val ru: String
) {


}
