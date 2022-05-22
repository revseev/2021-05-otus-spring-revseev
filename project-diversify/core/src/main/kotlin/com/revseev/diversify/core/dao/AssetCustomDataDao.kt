package com.revseev.diversify.core.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.revseev.diversify.core.domain.AssetCustomData
import mu.KotlinLogging
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

private val log = KotlinLogging.logger { }

@Repository
class AssetCustomDataDao(
    private val jdbc: NamedParameterJdbcTemplate,
    private val mapper: ObjectMapper
) {

    fun saveCustomData(userId: Int, figi: String, data: AssetCustomData) {
        log.info { "Adding data for userId = $userId, figi = $figi, data = $data into DB" }
        jdbc.update(
            """
            insert into asset_custom_data(user_id, asset_id, data)
            values (:userId, 
                    (select id from asset where figi = :figi), 
                    data::jsonb)""",
            MapSqlParameterSource("userId", userId)
                .addValue("figi", figi)
                .addValue("data", mapper.writeValueAsString(data))
        )
    }
}