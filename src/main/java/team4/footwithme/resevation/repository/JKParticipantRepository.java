package team4.footwithme.resevation.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team4.footwithme.resevation.domain.Participant;

import java.util.List;

@Repository
public interface JKParticipantRepository extends JpaRepository<Participant,Long> {
    @Query("select p from Participant p where p.isDeleted = 'false' and p.reservation.reservationId = :reservationId")
    List<Participant> findAllByReservationId(Long reservationId);
}
