package it.unibo.sap.ass02.persistence.repository

import it.unibo.sap.ass02.domain.ddd.Entity

interface Repository<K : Any, E : Entity<K>> {
    suspend fun save(entity: E) : Int?;
    suspend fun update(entity: E) : Int;
    suspend fun findAll() : Iterable<E>
    suspend fun findByID(id : K) : E?

    suspend fun delete(id: K): Int
}
