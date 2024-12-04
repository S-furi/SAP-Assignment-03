package it.unibo.sap.ass02.dashboard.controller

data class RideCommand(
    val userId: Int,
    val ebikeId: String,
    val command: Command,
) {
    enum class Command {
        START,
        STOP,
        SUBSCRIBE,
    }

    companion object {
        fun start(
            userId: Int,
            ebikeId: String,
        ) = RideCommand(userId, ebikeId, Command.START)

        fun stop(
            userId: Int,
            ebikeId: String,
        ) = RideCommand(userId, ebikeId, Command.STOP)

        fun subscribe(
            userId: Int,
            ebikeId: String,
        ) = RideCommand(userId, ebikeId, Command.SUBSCRIBE)
    }
}
