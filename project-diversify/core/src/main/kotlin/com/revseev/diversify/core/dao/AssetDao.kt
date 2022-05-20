package com.revseev.diversify.core.dao

import com.google.protobuf.util.JsonFormat
import com.revseev.diversify.core.domain.AssetType
import mu.KotlinLogging
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.tinkoff.piapi.contract.v1.Bond
import ru.tinkoff.piapi.contract.v1.Currency
import ru.tinkoff.piapi.contract.v1.Etf
import ru.tinkoff.piapi.contract.v1.Share

private val log = KotlinLogging.logger { }

@Repository
class AssetDao(
    private val jdbc: NamedParameterJdbcTemplate,
    private val protoPrinter: JsonFormat.Printer
) {

    fun loadShares(shares: List<Share>) {
        log.info { "Loading ${shares.size} Share records into DB" }
        jdbc.batchUpdate(
            """
            insert into asset(type, isin, figi, ticker, data) 
            values (:type, :isin, :figi, :ticker, :data ::jsonb)""",
            shares.map { share ->
                MapSqlParameterSource()
                    .addValue("type", AssetType.SHARE, java.sql.Types.OTHER)
                    .addValue("isin", share.isin)
                    .addValue("figi", share.figi)
                    .addValue("ticker", share.figi)
                    .addValue("data", protoPrinter.print(share))
            }
                .toTypedArray()
        )
    }

    fun loadEtfs(etfs: List<Etf>) {
        log.info { "Loading ${etfs.size} ETF records into DB" }
        jdbc.batchUpdate(
            """
            insert into asset(type, isin, figi, ticker, data) 
            values (:type, :isin, :figi, :ticker, :data ::jsonb)""",
            etfs.map { etf ->
                MapSqlParameterSource()
                    .addValue("type", AssetType.ETF, java.sql.Types.OTHER)
                    .addValue("isin", etf.isin)
                    .addValue("figi", etf.figi)
                    .addValue("ticker", etf.figi)
                    .addValue("data", protoPrinter.print(etf))
            }
                .toTypedArray()
        )
    }

    fun loadBonds(bonds: List<Bond>) {
        log.info { "Loading ${bonds.size} Bond records into DB" }
        jdbc.batchUpdate(
            """
            insert into asset(type, isin, figi, ticker, data) 
            values (:type, :isin, :figi, :ticker, :data ::jsonb)""",
            bonds.map { bond ->
                MapSqlParameterSource()
                    .addValue("type", AssetType.BOND, java.sql.Types.OTHER)
                    .addValue("isin", bond.isin)
                    .addValue("figi", bond.figi)
                    .addValue("ticker", bond.figi)
                    .addValue("data", protoPrinter.print(bond))
            }
                .toTypedArray()
        )
    }

    fun loadCurrencies(currencies: List<Currency>) {
        log.info { "Loading ${currencies.size} Currency records into DB" }
        jdbc.batchUpdate(
            """
            insert into asset(type, isin, figi, ticker, data) 
            values (:type, :isin, :figi, :ticker, :data ::jsonb)""",
            currencies.map { currency ->
                MapSqlParameterSource()
                    .addValue("type", AssetType.CURRENCY, java.sql.Types.OTHER)
                    .addValue("isin", currency.isin)
                    .addValue("figi", currency.figi)
                    .addValue("ticker", currency.figi)
                    .addValue("data", protoPrinter.print(currency))
            }
                .toTypedArray()
        )
    }
}