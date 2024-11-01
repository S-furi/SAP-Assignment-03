package it.unibo.sap.ass02.persistence.utils

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseUtils {
    private const val DB_NAME = "user-service"
    private const val USER = "roott"
    private const val PASSWORD = "test123"

    val database = Database.connect(
        url = "jdbc:postgresql://${System.getenv("POSTGRES_HOST") ?: "localhost"}:5432/$DB_NAME",
        user = USER,
        password = PASSWORD,
        driver = "org.postgresql.Driver",
    )


    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}