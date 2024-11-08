package org.example.persistence.repository

import org.example.app.Ride
import org.jetbrains.exposed.sql.Database

class RideRepository (database: Database): DatabaseRepository<Int, Ride>(database){
    override fun save(entity: Ride): Int? {
        TODO("Not yet implemented")
    }

    override fun update(entity: Ride): Int {
        TODO("Not yet implemented")
    }

    override fun findAll(): Iterable<Ride> {
        TODO("Not yet implemented")
    }

    override fun findByID(id: Int): Ride? {
        TODO("Not yet implemented")
    }

}
