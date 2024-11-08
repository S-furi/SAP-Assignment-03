package it.unibo.sap.ass02.persistence.repository

import it.unibo.sap.ass02.domain.ddd.Entity
import org.jetbrains.exposed.sql.Database

abstract class DatabaseRepository<K : Any, E: Entity<K>>(protected val database: Database) : Repository<K, E>
