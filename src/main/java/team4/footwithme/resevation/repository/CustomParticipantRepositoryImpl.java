package team4.footwithme.resevation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.resevation.domain.Participant;
import team4.footwithme.resevation.domain.ParticipantRole;

import java.util.List;

import static team4.footwithme.resevation.domain.QParticipant.participant;

public class CustomParticipantRepositoryImpl implements CustomParticipantRepository {
    private final JPAQueryFactory queryFactory;

    public CustomParticipantRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<Participant> findParticipantByReservationIdAndRole(Long reservationId) {
        return queryFactory
            .select(participant)
            .from(participant)
            .where(participant.isDeleted.eq(IsDeleted.FALSE)
                .and(participant.reservation.reservationId.eq(reservationId))
                .and(participant.participantRole.eq(ParticipantRole.MEMBER)
                    .or(participant.participantRole.eq(ParticipantRole.ACCEPT))))
            .fetch();
    }

    public List<Participant> findParticipantMercenaryByReservationId(Long reservationId) {
        return queryFactory
            .select(participant)
            .from(participant)
            .where(participant.isDeleted.eq(IsDeleted.FALSE)
                .and(participant.reservation.reservationId.eq(reservationId))
                .and(participant.participantRole.eq(ParticipantRole.PENDING)))
            .fetch();
    }
}
