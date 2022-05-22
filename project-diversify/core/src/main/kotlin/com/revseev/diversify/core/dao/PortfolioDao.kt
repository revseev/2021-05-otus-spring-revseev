package com.revseev.diversify.core.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.revseev.diversify.core.domain.PortfolioItem
import mu.KotlinLogging
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.tinkoff.piapi.core.models.Portfolio
import ru.tinkoff.piapi.core.models.Positions

private val log = KotlinLogging.logger { }

@Repository
class PortfolioDao(
    private val jdbc: NamedParameterJdbcTemplate,
    private val mapper: ObjectMapper,
) {

    fun savePortfolio(userId: Int, accountId: String, portfolio: Portfolio, positions: Positions) {
        log.info { "Saving portfolio and positions into DB for accountId = $accountId" }
        jdbc.update(
            """
            insert into portfolio(user_id, account_id, portfolio, positions) 
            values(:userId, :accountId, :portfolio::jsonb, :positions::jsonb)""",
            MapSqlParameterSource("userId", userId)
                .addValue("accountId", accountId)
                .addValue("portfolio", mapper.writeValueAsString(portfolio))
                .addValue("positions", mapper.writeValueAsString(positions))
        )
    }

    fun getPortfolioByUserId(userId: Int): List<PortfolioItem> = jdbc.query(
        """
        select prt.account_id
             , prt.asset_type
             , prt.figi
             , prt.quantity
             , prt.unitPrice
             , prt.currency
             , ast.data ->> 'name'                                                                 as name
             , coalesce(acust.data ->> 'countryOfRisk', ast.data ->> 'countryOfRisk', '-')         as country_of_risk_code
             , coalesce(acust.data ->> 'countryOfRiskName', ast.data ->> 'countryOfRiskName', '-') as country_of_risk
             , coalesce(acust.data ->> 'sector', ast.data ->> 'sector', '-')                       as sector
        from (select account_id                                                                             as account_id
                   , user_id
                   , jsonb_path_query(portfolio.portfolio, '$.positions.figi[*]') #>> '{}'                  as figi
                   , upper(jsonb_path_query(portfolio.portfolio, '$.positions.instrumentType[*]') #>> '{}') as asset_type
                   , (jsonb_path_query(portfolio.portfolio, '$.positions.quantity[*]'))::text               as quantity
                   , (jsonb_path_query(portfolio.portfolio, '$.positions.currentPrice.value[*]'))::text     as unitPrice
                   , jsonb_path_query(portfolio.portfolio, '$.positions.currentPrice.currency[*]') #>> '{}' as currency
              from portfolio
              where user_id = :userId
             ) as prt
                 join asset ast 
                    on ast.figi = prt.figi
                 left join asset_custom_data acust 
                    on acust.asset_id = ast.id and acust.user_id = prt.user_id
        order by account_id""",
        MapSqlParameterSource("userId", userId),
        mapTo {
            PortfolioItem(
                accountId = getString("account_id"),
                assetType = getStringAsEnum("asset_type"),
                figi = getString("figi"),
                name = getString("name"),
                countryOfRiskCode = getString("country_of_risk_code"),
                sector = getString("sector"),
                quantity = getString("quantity"),
                currency = getString("currency"),
                unitPrice = getString("unitPrice"),
            )
        }
    )

}