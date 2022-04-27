package ru.revseev.library.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

abstract class StringIdentifiable {
    @Id
    var id: String = ObjectId().toHexString()
}