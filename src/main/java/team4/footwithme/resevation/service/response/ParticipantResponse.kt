package team4.footwithme.resevation.service.response

import team4.footwithme.resevation.domain.Participant
import team4.footwithme.resevation.domain.ParticipantRole

@JvmRecord
data class ParticipantResponse(
    val participantId: Long?,
    val reservationId: Long?,
    val role: ParticipantRole?,
    val memberInfo: ParticipantMemberInfo
) {
    constructor(participant: Participant?) : this(
        participant!!.participantId,
        participant.reservation!!.reservationId,
        participant.participantRole,
        ParticipantMemberInfo(participant.member)
    )
}
