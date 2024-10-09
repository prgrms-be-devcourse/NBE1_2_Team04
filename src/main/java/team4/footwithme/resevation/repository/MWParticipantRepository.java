package team4.footwithme.resevation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team4.footwithme.resevation.domain.Participant;

import java.util.List;
import java.util.Optional;
@Repository
public interface MWParticipantRepository extends JpaRepository<Participant,Long> , CustomParticipantRepository{
    @Query("select p from Participant p where p.isDeleted = 'false' and p.reservation.reservationId = :id")
    List<Participant> findParticipantsByReservationId(@Param("id") Long reservationId);

    @Query("select p from Participant p where p.isDeleted = 'false' and p.reservation.reservationId = :rid and p.member.memberId = :mid")
    Optional<Participant> findParticipantsByReservationIdAndMemberId(@Param("rid") Long reservationId, @Param("mid") Long memberId);

    @Query("select p from Participant p where p.isDeleted = 'false' and p.participantId = :id")
    Optional<Participant> findParticipantsByParticipantId(@Param("id") Long participantId);
}
