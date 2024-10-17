package team4.footwithme.chat.service.event

import team4.footwithme.resevation.domain.Participant

@JvmRecord
data class ReservationMembersJoinEvent(
    val members: List<Participant>,
    val reservationId: Long?
)
