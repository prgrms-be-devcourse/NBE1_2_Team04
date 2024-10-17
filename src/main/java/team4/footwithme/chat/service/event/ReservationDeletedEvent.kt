package team4.footwithme.chat.service.event

@JvmRecord
data class ReservationDeletedEvent(
    val reservationId: Long
)
