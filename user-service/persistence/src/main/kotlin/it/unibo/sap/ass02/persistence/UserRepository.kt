package it.unibo.sap.ass02.persistence

import io.kotest.matchers.nulls.shouldNotBeNull
import it.unibo.sap.ass02.domain.User
import it.unibo.sap.ass02.domain.UserImpl
import it.unibo.sap.ass02.persistence.utils.DatabaseUtils.dbQuery
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class UserRepository(database: Database): DatabaseRepository<Int, User>(database) {
    object Users : Table() {
        val id = integer("id")
        val credit = integer("credit")

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    override suspend fun create(entity: User): Int? = dbQuery {
        if (this.findById(entity.id) == null) {
            Users.insert {
                it[id] = entity.id
                it[credit] = entity.credit
            }[Users.id]
        } else {
            null
        }
    }


    override suspend fun findById(id: Int): User? = dbQuery {
        Users.selectAll().where { Users.id eq id }
            .map { UserImpl(it[Users.id], it[Users.credit]) }
            .singleOrNull()
    }
    override suspend fun findAll(): Iterable<User> = dbQuery {
        Users.selectAll().map { UserImpl(it[Users.id], it[Users.credit]) }
    }

    override suspend fun delete(entity: User): User? = dbQuery {
        if (Users.deleteWhere { id eq entity.id } > 0) entity else null
    }

    override suspend fun update(entity: User): Boolean = dbQuery {
        Users.update({ Users.id eq entity.id }) {
            it[credit] = entity.credit
        } > 0
    }
}