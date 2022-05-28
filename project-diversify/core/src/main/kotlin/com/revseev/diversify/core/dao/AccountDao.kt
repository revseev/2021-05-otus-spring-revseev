package com.revseev.diversify.core.dao

import com.revseev.diversify.core.domain.Account
import mu.KotlinLogging
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Types

private val log = KotlinLogging.logger { }

@Repository
class AccountDao(
    private val jdbc: NamedParameterJdbcTemplate,
) {

    fun saveAccount(userId: Int, providerId: String, type: Account.Type, name: String) {
        log.info { "Saving account: id = $providerId" }
        jdbc.update(
            """
            insert into account (tkc_id, type, name, user_id)
                values (:tkc_id, :type, :name, :userId)
            on conflict (tkc_id) 
                do update set name = :name""",
            MapSqlParameterSource("tkc_id", providerId)
                .addValue("type", type, Types.OTHER)
                .addValue("name", name)
                .addValue("userId", userId)
        )
    }

    fun getAccountsByUserId(userId: Int): List<Account> = jdbc.query(
        """
        select tkc_id, name, type, user_id
            from account
        where user_id = :userId;""",
        MapSqlParameterSource("userId", userId),
        mapTo {
            Account(
                id = getString("tkc_id"),
                name = getString("name"),
                type = getStringAsEnum("type"),
                userId = getInt("user_id"),
            )
        }
    )

}