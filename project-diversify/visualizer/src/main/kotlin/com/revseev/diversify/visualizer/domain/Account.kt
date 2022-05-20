package com.revseev.diversify.visualizer.domain



data class Account(
    val providerId: String,
    val name: String,
    val type: Type,
    val userId: Int,
) {
    enum class Type {
        UNSPECIFIED, TINKOFF, TINKOFF_IIS, INVEST_BOX
    }
}
