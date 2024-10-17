package team4.footwithme.chat.service.event

@JvmRecord
data class ReservationPublishedEvent(
    val name: String,
    val reservationId: Long?
)
