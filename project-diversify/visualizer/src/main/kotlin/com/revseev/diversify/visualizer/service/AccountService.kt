package com.revseev.diversify.visualizer.service

import com.revseev.diversify.visualizer.domain.Account
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val coreClient: CoreClient,
) {

    fun getAccountsByUserId(userId: Int): List<Account> = coreClient.getPortfolioByUserId(userId).accounts
}
