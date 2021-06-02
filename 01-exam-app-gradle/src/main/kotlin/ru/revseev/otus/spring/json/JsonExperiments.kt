package ru.revseev.otus.spring.json

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun main() {
    val jsonString = """
        {
            "name": "kotlinx.serialization",
            "forks": [{"votes": 42}, {"votes": 9000}, {}]
        }
    """.trimIndent().also(::println)

    val element = Json.parseToJsonElement(jsonString)
    val jsonElement = element.jsonObject["forks"]!!.jsonArray[1]
    println(jsonElement)

}