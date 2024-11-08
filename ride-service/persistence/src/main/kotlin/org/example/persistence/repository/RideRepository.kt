package org.example.persistence.repository


import org.example.app.Ride
import org.example.persistence.repository.utils.DatabaseUtils
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

class RideRepository(database: Database = DatabaseUtils.database): DatabaseRepository<Int, Ride>(database){
    object Ride: Table() {
        val id = integer("id")
        val ebike = varchar("ebike_id", length = 100)
        val user = integer("user_id")
        val startDate = date("starting_date")
        val endingDate = date("ending_date")
        override val primaryKey = PrimaryKey(id)
    }

    override fun save(entity: org.example.app.Ride): Int? {
        TODO("Not yet implemented")
    }

    override fun update(entity: org.example.app.Ride): Int {
        TODO("Not yet implemented")
    }

    override fun findAll(): Iterable<org.example.app.Ride> {
        TODO("Not yet implemented")
    }

    override fun findByID(id: Int): org.example.app.Ride? {
        TODO("Not yet implemented")
    }
}
