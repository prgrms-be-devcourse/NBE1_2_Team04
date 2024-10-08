package team4.footwithme.resevation.repository;

import team4.footwithme.resevation.domain.Participant;

import java.util.List;

public interface CustomParticipantRepository {
    List<Participant> findParticipantByReservationIdAndRole(Long reservationId);

    List<Participant> findParticipantMercenaryByReservationId(Long reservationId);
}
