package ru.revseev.library.dao.impl

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.revseev.library.dao.GenreDao
import ru.revseev.library.domain.Genre
import ru.revseev.library.exception.DataNotFoundException

@Repository
class GenreDaoImpl(private val jdbc: NamedParameterJdbcTemplate) : GenreDao {

    override fun getAll(): List<Genre> {
        val sql = "SELECT id, name FROM genre"

        return jdbc.query(sql) { rs, _ ->
            Genre(rs.getLong("id"), rs.getString("name"))
        }
    }

    override fun getById(id: Long): Genre {
        val sql = "SELECT id, name FROM genre WHERE id =:id"

        return getNonNullableData("Genre with id = $id was not found") {
            jdbc.queryForObject(sql, MapSqlParameterSource("id", id), Genre::class.java)
        }
    }

    override fun add(genre: Genre): Genre {
        TODO("Not yet implemented")
    }

    override fun update(genre: Genre): Genre {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }
}

inline fun <reified T> getNonNullableData(errorMessage: String, action: () -> T?): T {
    val t: T? = try {
        action.invoke()
    } catch (ex: EmptyResultDataAccessException) {
        throw DataNotFoundException(errorMessage, ex)
    }
    return t ?: throw DataNotFoundException(errorMessage)
}