package com.revseev.diversify.core.domain

import ru.tinkoff.piapi.contract.v1.AccountType as TinkoffAccountType


data class Account(
    val id: String,
    val name: String,
    val type: Type,
    val userId: Int,
) {
    enum class Type {
        UNSPECIFIED, TINKOFF, TINKOFF_IIS, INVEST_BOX;

        companion object {
            fun from(tinkoffType: TinkoffAccountType) = when (tinkoffType) {
                TinkoffAccountType.ACCOUNT_TYPE_TINKOFF -> TINKOFF
                TinkoffAccountType.ACCOUNT_TYPE_TINKOFF_IIS -> TINKOFF_IIS
                TinkoffAccountType.ACCOUNT_TYPE_INVEST_BOX -> INVEST_BOX
                else -> UNSPECIFIED
            }
        }
    }
}
