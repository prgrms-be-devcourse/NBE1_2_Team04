package team4.footwithme.resevation.service.response

import team4.footwithme.resevation.domain.Participant
import team4.footwithme.resevation.domain.ParticipantRole

// TODO :: ParticipantInfoResponse 로 변경 부탁
@JvmRecord
data class ParticipantInfoResponse(
    val memberName: String?,
    val role: ParticipantRole?
) {
    companion object {
        fun from(participant: Participant?): ParticipantInfoResponse {
            return ParticipantInfoResponse(
                participant!!.member!!.name,
                participant.participantRole
            )
        }
    }
}
