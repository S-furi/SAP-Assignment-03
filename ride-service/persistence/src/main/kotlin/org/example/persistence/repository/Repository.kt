package org.example.persistence.repository
import org.example.app.Entity

interface Repository<K : Any, E : Entity<K>> {
    suspend fun save(entity: E) : Int?;
    suspend fun update(entity: E) : Int;
    suspend fun findAll() : Iterable<E>
    suspend fun findByID(id : K) : E?
}
