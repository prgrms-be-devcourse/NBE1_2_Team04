package team4.footwithme.resevation.repository

import team4.footwithme.resevation.domain.Participant

interface CustomParticipantRepository {
    fun findParticipantByReservationIdAndRole(reservationId: Long?): List<Participant>?

    fun findParticipantMercenaryByReservationId(reservationId: Long?): List<Participant>?
}
