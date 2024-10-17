package team4.footwithme.resevation.service

import team4.footwithme.member.domain.Member
import team4.footwithme.resevation.service.request.ParticipantUpdateServiceRequest
import team4.footwithme.resevation.service.response.ParticipantResponse

interface ParticipantService {
    fun createMercenaryParticipant(mercenaryId: Long?, member: Member?): ParticipantResponse

    fun createParticipant(reservationId: Long?, member: Member?): ParticipantResponse

    fun deleteParticipant(reservationId: Long, member: Member?): Long?

    fun updateMercenaryParticipant(request: ParticipantUpdateServiceRequest?, member: Member?): ParticipantResponse

    fun getAcceptParticipants(reservationId: Long?): List<ParticipantResponse>?

    fun getParticipants(reservationId: Long?): List<ParticipantResponse>?

    fun getParticipantsMercenary(reservationId: Long?): List<ParticipantResponse>?
}
