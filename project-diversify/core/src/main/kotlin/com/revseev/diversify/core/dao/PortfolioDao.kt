package com.revseev.diversify.core.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.revseev.diversify.core.domain.PortfolioItem
import mu.KotlinLogging
import org.javamoney.moneta.FastMoney
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.tinkoff.piapi.core.models.Portfolio
import ru.tinkoff.piapi.core.models.Positions
import java.math.BigDecimal

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
        select portf.account_id
             , portf.asset_type
             , portf.figi
             , portf.quantity
             , portf.unitPrice
             , portf.currency
             , ast.data ->> 'name'                             as name
             , coalesce(ast.data ->> 'countryOfRisk', '-')     as country_of_risk_code
             , coalesce(ast.data ->> 'sector', '-')            as sector
        from (select account_id                                                                             as account_id
                   , jsonb_path_query(portfolio.portfolio, '$.positions.figi[*]') #>> '{}'                  as figi
                   , upper(jsonb_path_query(portfolio.portfolio, '$.positions.instrumentType[*]') #>> '{}') as asset_type
                   , (jsonb_path_query(portfolio.portfolio, '$.positions.quantity[*]'))::text               as quantity
                   , (jsonb_path_query(portfolio.portfolio, '$.positions.currentPrice.value[*]'))::text     as unitPrice
                   , jsonb_path_query(portfolio.portfolio, '$.positions.currentPrice.currency[*]') #>> '{}' as currency
              from portfolio
              where user_id = :userId
             ) as portf
                 join asset ast on ast.figi = portf.figi
        order by account_id""",
        MapSqlParameterSource("userId", userId),
        mapTo {
            val quantity = BigDecimal(getString("quantity"))
            val unitPrice = FastMoney.of(getBigDecimal("unitPrice"), getString("currency"))
            PortfolioItem(
                accountId = getString("account_id"),
                assetType = getStringAsEnum("asset_type"),
                figi = getString("figi"),
                name = getString("name"),
                countryOfRiskCode = getString("country_of_risk_code"),
                sector = getString("sector"),
                quantity = quantity,
                unitPrice = unitPrice,
                totalPrice = unitPrice.multiply(quantity)
            )
        }
    )

}