package it.unibo.sap.ass02.persistence.repository

import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.domain.RideImpl
import it.unibo.sap.ass02.domain.model.EBikeImpl
import it.unibo.sap.ass02.domain.model.UserImpl
import it.unibo.sap.ass02.persistence.repository.utils.DatabaseUtils
import it.unibo.sap.ass02.persistence.repository.utils.DatabaseUtils.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.transaction

class RideRepository(
    database: Database = DatabaseUtils.database,
) : DatabaseRepository<Int, Ride>(database) {
    object Rides : Table() {
        val id = integer("id")
        val ebike = varchar("ebike_id", length = 100)
        val user = integer("user_id")
        val startDate = date("starting_date").nullable()
        val endingDate = date("ending_date").nullable()
        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(this.database) {
            SchemaUtils.create(Rides)
        }
    }

    override suspend fun save(entity: Ride): Int? =
        dbQuery {
            Rides.insert {
                it[id] = entity.id
                it[ebike] = entity.ebike.id
                it[user] = entity.user.id
            }[Rides.id]
        }

    override suspend fun update(entity: Ride): Int? =
        dbQuery {
            this.findByID(entity.id)?.let { _ ->
                Rides.update({ Rides.id eq entity.id }) {
                    it[startDate] = entity.startedDate
                    it[endingDate] = entity.endDate
                }
            }
        }

    override suspend fun findAll(): Iterable<Ride> =
        dbQuery {
            Rides
                .selectAll()
                .map {
                    RideImpl(
                        EBikeImpl(it[Rides.ebike]),
                        UserImpl(it[Rides.user]),
                        it[Rides.startDate],
                        it[Rides.endingDate],
                        it[Rides.id],
                    )
                }
        } ?: listOf()

    override suspend fun delete(id: Int): Int? =
        dbQuery {
            this.findByID(id)?.let { _ ->
                Rides.deleteWhere { Rides.id eq id }
            }
        }

    override suspend fun findByID(id: Int): Ride? =
        dbQuery {
            Rides
                .selectAll()
                .where { Rides.id eq id }
                .map {
                    RideImpl(
                        EBikeImpl(it[Rides.ebike]),
                        UserImpl(it[Rides.user]),
                        it[Rides.startDate],
                        it[Rides.endingDate],
                        it[Rides.id],
                    )
                }.singleOrNull()
        }

    suspend fun findByUserAndBike(
        userId: Int,
        ebikeId: String,
    ): Ride? =
        dbQuery {
            Rides
                .selectAll()
                .where {
                    (Rides.user eq userId) and (Rides.ebike eq ebikeId) and Rides.endingDate.isNull()
                }.map {
                    RideImpl(
                        EBikeImpl(it[Rides.ebike]),
                        UserImpl(it[Rides.user]),
                        it[Rides.startDate],
                        it[Rides.endingDate],
                        it[Rides.id],
                    )
                }.singleOrNull()
        }

    suspend fun getLastId(): Int = findAll().maxByOrNull { it.id }?.id ?: 0
}
