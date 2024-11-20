package it.unibo.sap.ass02.persistence.repository.utils

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseUtils {
    private const val DB_NAME = "ANAME"
    private const val DB_USER = "AUSER"
    private const val DB_PASSWORD = "APASSWORD"

    val database =
        Database.connect(
            url = "jdbc:postgresql://${System.getenv("POSTGRES_HOST") ?: "localhost"}:5432/$DB_NAME",
            user = DB_USER,
            password = DB_PASSWORD,
        )

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}
