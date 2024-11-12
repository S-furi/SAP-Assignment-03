package it.unibo.sap.ass02.persistence.repository


import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.domain.RideImpl
import it.unibo.sap.ass02.domain.model.stub.EBikeImpl
import it.unibo.sap.ass02.domain.model.stub.UserImpl
import it.unibo.sap.ass02.persistence.repository.utils.DatabaseUtils
import it.unibo.sap.ass02.persistence.repository.utils.DatabaseUtils.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.transaction

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
        return dbQuery {
                Rides.update({ Rides.id eq entity.id }) {
                    it[endingDate] = entity.endDate
                }
        }
    }

    override suspend fun findAll(): Iterable<Ride> {
        return dbQuery {
            Rides.selectAll()
                .map { RideImpl(EBikeImpl(it[Rides.ebike]), UserImpl(it[Rides.user]), it[Rides.startDate], it[Rides.endingDate], it[Rides.id]) }
        }
    }

    override suspend fun delete(id: Int): Int? {
        return dbQuery {
            if (this.findByID(id) != null) {
                Rides.deleteWhere { Rides.id eq id }
            }else {
                null
            }
        }
    }

    override suspend fun findByID(id: Int): Ride? {
        return dbQuery {
            Rides.selectAll()
                .where {Rides.id eq id }
                .map { RideImpl(EBikeImpl(it[Rides.ebike]), UserImpl(it[Rides.user]), it[Rides.startDate], it[Rides.endingDate], it[Rides.id]) }
                .singleOrNull()
        }
    }


}
