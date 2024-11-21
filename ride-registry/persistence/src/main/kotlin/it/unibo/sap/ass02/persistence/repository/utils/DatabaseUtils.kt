package it.unibo.sap.ass02.persistence.repository.utils

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object DatabaseUtils {
    private const val DB_NAME = "ride-repo"
    private const val DB_USER = "postgres"
    private const val DB_PASSWORD = "postgres"
    private val logger: Logger = LoggerFactory.getLogger(DatabaseUtils::class.java)

    val database =
        Database.connect(
            url = "jdbc:postgresql://${System.getenv("POSTGRES_HOST") ?: "localhost"}:5432/$DB_NAME",
            user = DB_USER,
            password = DB_PASSWORD,
        )

    suspend fun <T> dbQuery(block: suspend () -> T): T? =
        runCatching {
            newSuspendedTransaction(Dispatchers.IO) { block() }
        }.getOrElse { e ->
            logger.error(e.message)
            null
        }
}
