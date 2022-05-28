package com.revseev.diversify.visualizer.service

import com.revseev.diversify.visualizer.domain.AssetCustomData
import org.springframework.stereotype.Service
import java.util.concurrent.*

@Service
class AssetCustomDataStorage {

    private val storage = ConcurrentHashMap<Int, ConcurrentHashMap<String, AssetCustomData>>()

    fun forUser(userId: Int): ConcurrentHashMap<String, AssetCustomData> =
        storage.getOrPut(userId) { ConcurrentHashMap<String, AssetCustomData>() }

}

