package it.unibo.sap.ass02.service.api

object RideRoutes {
    private const val BASE_PATH = "/rides"
    const val HEALTH = "$BASE_PATH/health"
    const val ALL_RIDES = "$BASE_PATH/all"
    const val RIDE_BY_ID = "$BASE_PATH/{id}"
    const val RIDE_BY_USER_AND_BIKE = "$BASE_PATH/find"
    const val CREATE_RIDE = "$BASE_PATH/create"
    const val UPDATE_RIDE = "$BASE_PATH/{id}"
    const val DELETE_RIDE = "$BASE_PATH/{id}"
    const val START_RIDE = "$BASE_PATH/{id}/start"
    const val END_RIDE = "$BASE_PATH/{id}/stop"
}
