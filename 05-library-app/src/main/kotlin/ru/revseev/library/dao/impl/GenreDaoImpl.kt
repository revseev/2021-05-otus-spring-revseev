package ru.revseev.library.dao.impl

import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.revseev.library.dao.GenreDao
import ru.revseev.library.dao.wrapExceptions
import ru.revseev.library.domain.Genre

@Repository
class GenreDaoImpl(private val jdbc: NamedParameterJdbcTemplate) : GenreDao {

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

    override fun add(genre: Genre): Boolean {
        val params = MapSqlParameterSource("name", genre.name)
        val sql = "INSERT INTO genres(name) VALUES (:name)"

        return wrapExceptions("Error adding Genre: {id = ${genre.id}, name = ${genre.name}}") {
            try {
                jdbc.update(sql, params) > 0
            } catch (alreadyExists: DuplicateKeyException) {
                false
            }
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