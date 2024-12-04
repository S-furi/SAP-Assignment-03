package it.unibo.sap.ass02.persistence.utls

import it.unibo.sap.ass02.domain.User
import it.unibo.sap.ass02.persistence.AsyncRepository

class DumbUserDB : AsyncRepository<Int, User> {
    private val users = mutableSetOf<User>()

    override suspend fun create(entity: User): Int? =
        if (users.add(entity)) entity.id else null


    override suspend fun findById(id: Int): User? = users.find { it.id == id }

    override suspend fun findAll(): Iterable<User> = users

    override suspend fun update(entity: User): Boolean = users.takeIf {
            users.contains(entity)
        }?.let {
            it.remove(entity)
            it.add(entity)
        } ?: false

    override suspend fun delete(entity: User): User? =
        if (users.remove(entity)) entity else null
}