package org.example.persistence.repository
import org.example.app.Entity

interface Repository<K : Any, E : Entity<K>> {
    fun save(entity: E) : Int?;
    fun update(entity: E) : Int;
    fun findAll() : Iterable<E>
    fun findByID(id : K) : E?
}
