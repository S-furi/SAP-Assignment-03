package it.unibo.sap.ass02.persistence

import it.unibo.sap.ass02.domain.ddd.Entity

interface AsyncRepository<I : Any, E : Entity<I>> {
    suspend fun create(entity: E): I?
    suspend fun findById(id: I): E?
    suspend fun findAll(): Iterable<E>
    suspend fun update(entity: E): Boolean
    suspend fun delete(entity: E): E?
}