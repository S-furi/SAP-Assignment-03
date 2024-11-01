package it.unibo.sap.ass02.persistence

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.unibo.sap.ass02.domain.UserImpl
import it.unibo.sap.ass02.persistence.utils.DatabaseUtils
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class UserDatabaseTest : FunSpec({
    afterEach {
        transaction(DatabaseUtils.database) {
            SchemaUtils.drop(UserRepository.Users)
        }
    }


    test("Some operations should work") {
        val userRepository = UserRepository(DatabaseUtils.database)
        val u = UserImpl(1)
        userRepository.create(u) shouldBe 1
        userRepository.findById(1) shouldBe u
        userRepository.findAll() shouldBe listOf(u)
        userRepository.update(UserImpl(1, 1000)) shouldBe true
        userRepository.findById(1)?.credit shouldBe 1000
        userRepository.delete(u)?.id shouldBe 1
        userRepository.findById(1) shouldBe null
    }
})