package com.revseev.diversify.core.dao

import java.sql.ResultSet


inline fun <reified T> mapTo(crossinline conversion: ResultSet.() -> T): (ResultSet, Int) -> T =
    { rs, _ -> rs.conversion() }

inline fun <reified T : Enum<T>> ResultSet.getStringAsEnum(column: String): T = enumValueOf(getString(column))