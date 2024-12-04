package it.unibo.sap.ass02.service.api

import it.unibo.sap.ass02.domain.User
import it.unibo.sap.ass02.persistence.UserRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object UserResolver {
    private val repository = UserRepository()

    suspend fun getAllUsers(): List<User> = repository.findAll().toList()

    suspend fun getUserById(id: Int) = repository.findById(id)

    suspend fun createUser(user: User) = repository.create(user)

    suspend fun deleteUser(id: Int) = repository.findById(id)?.let { repository.delete(it) }

    suspend fun decreaseCredit(
        id: Int,
        amount: Int,
    ) = repository.findById(id)?.let {
        it.decreaseCredit(amount)
        repository.update(it)
    }

    suspend fun increaseCredit(
        id: Int,
        amount: Int,
    ) = repository.findById(id)?.let {
        it.rechargeCredit(amount)
        repository.update(it)
    }
}
