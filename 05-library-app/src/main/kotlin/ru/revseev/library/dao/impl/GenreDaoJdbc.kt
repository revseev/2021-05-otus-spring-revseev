package ru.revseev.library.dao.impl

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import ru.revseev.library.dao.GenreDao
import ru.revseev.library.dao.uniqueResult
import ru.revseev.library.dao.wrapExceptions
import ru.revseev.library.dao.wrapExceptionsNullable
import ru.revseev.library.domain.Genre

@Repository
class GenreDaoJdbc(private val jdbc: NamedParameterJdbcTemplate) : GenreDao {

    override fun getAll(): List<Genre> {
        val sql = "SELECT id, name FROM genres"

        return wrapExceptions("Error getting all Genres") {
            jdbc.query(sql) { rs, _ -> Genre(rs.getLong("id"), rs.getString("name")) }
        }
    }

    override fun getById(id: Long): Genre {
        val params = MapSqlParameterSource("id", id)
        val sql = "SELECT id, name FROM genres WHERE id = :id"

        return wrapExceptions("Error getting Genre with id = $id") {
            jdbc.queryForObject(sql, params) { rs, _ ->
                Genre(rs.getLong("id"), rs.getString("name"))
            }
        }
    }

    override fun getByName(name: String): Genre? {
        val params = MapSqlParameterSource("name", name)
        val sql = "SELECT id, name FROM genres WHERE name = :name"

        return wrapExceptionsNullable("Error getting Genre with name = $name") {
            jdbc.query(sql, params) { rs, _ ->
                Genre(rs.getLong("id"), rs.getString("name"))
            }.uniqueResult()
        }
    }

    override fun add(genre: Genre): Long {
        if (genre.id != null) {
            return genre.id!!
        }
        return getByName(genre.name)?.id ?: addNew(genre)
    }

    private fun addNew(genre: Genre): Long {
        val params = MapSqlParameterSource("name", genre.name)
        val keyHolder = GeneratedKeyHolder()
        val sql = "INSERT INTO genres(name) VALUES (:name)"

        return wrapExceptions("Error adding Genre: {id = ${genre.id}, name = ${genre.name}}") {
            jdbc.update(sql, params, keyHolder)
            keyHolder.key as Long
        }
    }

    override fun update(genre: Genre): Boolean {
        val sql = "UPDATE genres SET name = :name WHERE id = :id"

        return wrapExceptions("Error updating Genre: {id = ${genre.id}, name = ${genre.name}}") {
            jdbc.update(sql, BeanPropertySqlParameterSource(genre)) > 0
        }
    }

    override fun deleteById(id: Long): Boolean {
        val sql = "DELETE FROM genres WHERE id = :id"

        return wrapExceptions("Error deleting Genre by id = $id") {
            jdbc.update(sql, MapSqlParameterSource("id", id)) > 0
        }
    }
}