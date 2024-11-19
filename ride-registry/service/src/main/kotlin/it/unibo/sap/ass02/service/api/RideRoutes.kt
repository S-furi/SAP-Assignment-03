package it.unibo.sap.ass02.service.api

object RideRoutes {
    private const val BASE_PATH = "/api/rides"
    const val ALL_RIDES = "$BASE_PATH/all"
    const val RIDE_BY_ID = "$BASE_PATH/find/{id}"
    const val CREATE_RIDE = "$BASE_PATH/create"
    const val UPDATE_RIDE = "$BASE_PATH/{id}"
    const val DELETE_RIDE = "$BASE_PATH/{id}"
}
