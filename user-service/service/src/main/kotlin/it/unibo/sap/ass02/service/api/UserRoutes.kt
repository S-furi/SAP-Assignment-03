@file:Suppress("ktlint:standard:filename")

package it.unibo.sap.ass02.service.api

object UsersRoutes {
    private const val BASE_PATH: String = "api/users"
    const val USERS: String = "$BASE_PATH/all"
    const val USER_BY_ID: String = "$BASE_PATH/find/{id}"
    const val USER_ADD: String = "$BASE_PATH/new"
    const val USER_DELETE: String = "$BASE_PATH/{id}"
    const val USER_INCREASE_CREDIT: String = "$BASE_PATH/increase-credit"
    const val USER_DECREASE_CREDIT: String = "$BASE_PATH/decrease-credit"
}
