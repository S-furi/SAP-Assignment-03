package it.unibo.sap.ass02.persistence

import it.unibo.sap.ass02.domain.ddd.Entity
import org.jetbrains.exposed.sql.Database

abstract class DatabaseRepository<I : Any, E : Entity<I>>(protected val database: Database) : AsyncRepository<I, E>
