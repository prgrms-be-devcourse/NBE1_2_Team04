package team4.footwithme.resevation.service.request

import team4.footwithme.resevation.domain.ParticipantRole

@JvmRecord
data class ParticipantUpdateServiceRequest(
    val participantId: Long?,
    val role: ParticipantRole?
) {
}
