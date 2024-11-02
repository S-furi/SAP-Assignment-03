package it.unibo.sap.ass02.service.api

object UsersRoutes {
    private const val BASE_PATH: String = "/user"
    const val USERS: String = "$BASE_PATH/all"
    const val USER_BY_ID: String = "$BASE_PATH/find/{id}"
    const val USER_INCREASE_CREDIT: String = "$BASE_PATH/user/{id}/increase-credit"
    const val USER_DECREASE_CREDIT: String = "$BASE_PATH/user/{id}/decrease-credit"
}