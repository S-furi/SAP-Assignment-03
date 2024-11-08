package org.example.persistence.repository

import org.example.app.Entity
import org.jetbrains.exposed.sql.Database

abstract class DatabaseRepository<K : Any, E: Entity<K>>(protected val database: Database) : Repository<K, E>
