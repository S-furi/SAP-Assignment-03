package it.unibo.sap.ass02.api

object ServicesURIs {
    private val isProd = System.getenv("PROD")?.isNotEmpty() ?: false

    private val servicePorts =
        mapOf(
            "v" to (System.getenv("VEHICLE_SERVICE_PORT")?.toInt() ?: 1926),
            "u" to (System.getenv("USER_SERVICE_PORT")?.toInt() ?: 11000),
            "rs" to (System.getenv("RIDE_SERVICE_PORT")?.toInt() ?: 9990),
            "rr" to (System.getenv("RIDE_REGISTRY_PORT")?.toInt() ?: 9991),
        )

    private val serviceHostnames =
        mapOf(
            "v" to if (isProd) (System.getenv("VEHICLE_SERVICE_HOST") ?: "vehicle-service") else "localhost",
            "u" to if (isProd) (System.getenv("USER_SERVICE_HOST") ?: "user-service") else "localhost",
            "rs" to if (isProd) (System.getenv("RIDE_SERVICE_HOST") ?: "ride-service") else "localhost",
            "rr" to if (isProd) (System.getenv("RIDE_REGISTRY_HOST") ?: "ride-registry") else "localhost",
        )

    private fun buildUri(
        host: String,
        port: Int,
    ) = "http://$host:$port"

    val VEHICLE_SERVICE = buildUri(serviceHostnames["v"]!!, servicePorts["v"]!!)
    val USER_SERVICE = buildUri(serviceHostnames["u"]!!, servicePorts["u"]!!)
    val RIDE_SERVICE = buildUri(serviceHostnames["rs"]!!, servicePorts["rs"]!!)
    val RIDE_REGISTRY = buildUri(serviceHostnames["rr"]!!, servicePorts["rr"]!!)
}
