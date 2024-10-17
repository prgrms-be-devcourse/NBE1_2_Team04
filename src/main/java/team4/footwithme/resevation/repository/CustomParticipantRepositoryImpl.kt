package team4.footwithme.resevation.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import team4.footwithme.global.domain.IsDeleted
import team4.footwithme.resevation.domain.Participant
import team4.footwithme.resevation.domain.ParticipantRole
import team4.footwithme.resevation.domain.QParticipant

class CustomParticipantRepositoryImpl(private val queryFactory: JPAQueryFactory) : CustomParticipantRepository {
    override fun findParticipantByReservationIdAndRole(reservationId: Long?): List<Participant>? {
        return queryFactory
            .select(QParticipant.participant)
            .from(QParticipant.participant)
            .where(
                QParticipant.participant.isDeleted.eq(IsDeleted.FALSE)
                    .and(QParticipant.participant.reservation.reservationId.eq(reservationId))
                    .and(
                        QParticipant.participant.participantRole.eq(ParticipantRole.MEMBER)
                            .or(QParticipant.participant.participantRole.eq(ParticipantRole.ACCEPT))
                    )
            )
            .fetch()
    }

    override fun findParticipantMercenaryByReservationId(reservationId: Long?): List<Participant>? {
        return queryFactory
            .select(QParticipant.participant)
            .from(QParticipant.participant)
            .where(
                QParticipant.participant.isDeleted.eq(IsDeleted.FALSE)
                    .and(QParticipant.participant.reservation.reservationId.eq(reservationId))
                    .and(QParticipant.participant.participantRole.eq(ParticipantRole.PENDING))
            )
            .fetch()
    }
}
