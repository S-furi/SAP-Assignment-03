package org.example.persistence.repository


import kotlinx.serialization.json.internal.decodeByReader
import org.example.app.Ride
import org.example.persistence.repository.utils.DatabaseUtils
import org.example.persistence.repository.utils.DatabaseUtils.dbQuery
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

class RideRepository(database: Database = DatabaseUtils.database): DatabaseRepository<Int, Ride>(database){
    object Rides: Table() {
        val id = integer("id")
        val ebike = varchar("ebike_id", length = 100)
        val user = integer("user_id")
        val startDate = date("starting_date")
        val endingDate = date("ending_date")
        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(this.database){
            SchemaUtils.create(Rides)
        }
    }

    override suspend fun save(entity: Ride): Int? = dbQuery {
        if (this.findByID(entity.getId()) == null) {
            Rides.insert {
                it[id] = entity.getId()
                it[ebike] = ""
                it[user] = 0
                it[startDate] = LocalDate.now()
                it[endingDate] = LocalDate.now()
            }[Rides.id]
        }else{
            null
        }
    }

    override suspend fun update(entity: Ride): Int {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(): Iterable<Ride> {
        TODO("Not yet implemented")
    }

    override suspend fun findByID(id: Int): Ride? {
        TODO("Not yet implemented")
    }
}
