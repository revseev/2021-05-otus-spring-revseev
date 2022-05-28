package com.revseev.diversify.core.service

import com.revseev.diversify.core.dao.AssetCustomDataDao
import com.revseev.diversify.core.domain.AssetCustomData
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger { }


@Service
class AssetCustomDataService(
    private val acdDao: AssetCustomDataDao,
) {

    @Transactional
    fun saveCustomData(userId: Int, figi: String, data: AssetCustomData) {
        log.info { "Saving custom data for user = $userId" }
        acdDao.saveCustomData(userId, figi, data)
    }
}