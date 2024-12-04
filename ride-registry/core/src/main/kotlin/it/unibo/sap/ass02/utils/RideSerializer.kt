package it.unibo.sap.ass02.utils

import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.domain.RideImpl
import it.unibo.sap.ass02.domain.model.EBikeImpl
import it.unibo.sap.ass02.domain.model.UserImpl
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate

object RideSerializer : KSerializer<Ride> {
    @Serializable
    @SerialName("Ride")
    private data class RideSurrogate(
        val rideId: Int,
        val ebike: String,
        val user: Int,
        @Serializable(with = LocalDateSerializer::class)
        val startedDate: LocalDate?,
        @Serializable(with = LocalDateSerializer::class)
        val endDate: LocalDate?,
    )

    override val descriptor: SerialDescriptor = RideSurrogate.serializer().descriptor

    override fun serialize(
        encoder: Encoder,
        value: Ride,
    ) {
        val surrogate = RideSurrogate(value.id, value.ebike.id, value.user.id, value.startedDate, value.endDate)
        encoder.encodeSerializableValue(RideSurrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): Ride {
        val surrogate = decoder.decodeSerializableValue(RideSurrogate.serializer())
        return RideImpl(
            EBikeImpl(surrogate.ebike),
            UserImpl(surrogate.user),
            surrogate.startedDate,
            surrogate.endDate,
            surrogate.rideId,
        )
    }
}
