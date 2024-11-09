package it.unibo.sap.ass02.persistence.repository


import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.persistence.repository.utils.DatabaseUtils
import it.unibo.sap.ass02.persistence.repository.utils.DatabaseUtils.dbQuery
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
        val endingDate = date("ending_date").nullable()
        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(this.database){
            SchemaUtils.create(Rides)
        }
    }

    override suspend fun save(entity: Ride): Int? = dbQuery {
        if (this.findByID(entity.id) == null) {
            Rides.insert {
                it[id] = entity.id
                it[ebike] = entity.ebike.id
                it[user] = entity.user.id
                it[startDate] = entity.startedDate
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
