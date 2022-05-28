package com.revseev.diversify.core.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.protobuf.util.JsonFormat
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SerializationConfig {

    @Bean
    fun objectMapper(): ObjectMapper = jacksonObjectMapper().disable(
        SerializationFeature.FAIL_ON_EMPTY_BEANS
    )

    @Bean
    fun protobufPrinter(): JsonFormat.Printer = JsonFormat.printer()
        .omittingInsignificantWhitespace()

}