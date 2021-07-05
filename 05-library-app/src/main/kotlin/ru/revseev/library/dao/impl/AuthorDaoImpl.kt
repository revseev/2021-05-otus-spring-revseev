package ru.revseev.library.dao.impl

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import ru.revseev.library.dao.AuthorDao
import ru.revseev.library.dao.wrapExceptions
import ru.revseev.library.domain.Author

@Repository
class AuthorDaoImpl(private val jdbc: NamedParameterJdbcTemplate) : AuthorDao {

    override fun getAll(): List<Author> {
        val sql = "SELECT id, name FROM authors"

        return wrapExceptions("Error getting all Authors") {
            jdbc.query(sql) { rs, _ -> Author(rs.getLong("id"), rs.getString("name")) }
        }
    }

    override fun getById(id: Long): Author {
        val params = MapSqlParameterSource("id", id)
        val sql = "SELECT id, name FROM authors WHERE id = :id"

        return wrapExceptions("Error getting Author with id = $id") {
            jdbc.queryForObject(sql, params) { rs, _ ->
                Author(rs.getLong("id"), rs.getString("name"))
            }
        }
    }

    override fun add(author: Author): Long {
        if (author.id != null) {
            return author.id!!
        }
        val params = MapSqlParameterSource("name", author.name)
        val keyHolder = GeneratedKeyHolder()
        val sql = "INSERT INTO authors(name) VALUES (:name)"

        return wrapExceptions("Error adding Author: {id = ${author.id}, name = ${author.name}}") {
            jdbc.update(sql, params, keyHolder)
            keyHolder.key as Long
        }
    }

    override fun update(author: Author): Boolean {
        val sql = "UPDATE authors SET name = :name WHERE id = :id"

        return wrapExceptions("Error updating Author: {id = ${author.id}, name = ${author.name}}") {
            jdbc.update(sql, BeanPropertySqlParameterSource(author)) > 0
        }
    }

    override fun deleteById(id: Long): Boolean {
        val sql = "DELETE FROM authors WHERE id = :id"

        return wrapExceptions("Error deleting Author by id = $id") {
            jdbc.update(sql, MapSqlParameterSource("id", id)) > 0
        }
    }
}