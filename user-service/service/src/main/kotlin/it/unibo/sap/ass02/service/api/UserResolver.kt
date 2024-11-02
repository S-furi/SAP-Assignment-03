package it.unibo.sap.ass02.service.api

import it.unibo.sap.ass02.domain.User
import it.unibo.sap.ass02.persistence.UserRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object UserResolver {
    private val repository = UserRepository()

    suspend fun getAllUsers() = repository.findAll().map { it.jsonEncode() }
    suspend fun getUserById(id: Int) = repository.findById(id)?.jsonEncode()

    private fun User.jsonEncode() = Json.encodeToString(this)
}