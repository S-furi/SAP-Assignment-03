package it.unibo.sap.ass02.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserSerialization : StringSpec({
    "A user should be serialized properly" {
        val u: User = UserImpl(1)
        val encoded = Json.encodeToString(u);
        Json.decodeFromString<User>(encoded) shouldBe u
    }
})