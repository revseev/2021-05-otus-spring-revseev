package com.revseev.diversify.visualizer.domain

import java.util.*

data class FilterConfig(
    val accountFilter: Set<String>? = null,
    val assetTypeFilter: EnumSet<AssetType>? = null,
    val countryOfRiskFilter: Set<String>? = null,
    val sectorFilter: Set<String>? = null,
)