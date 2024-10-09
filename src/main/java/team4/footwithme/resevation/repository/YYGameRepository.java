package team4.footwithme.resevation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team4.footwithme.resevation.domain.Game;
import team4.footwithme.resevation.domain.Reservation;

import java.util.Optional;

@Repository
public interface YYGameRepository extends JpaRepository<Game, Long> {
    @Query("select g.firstTeamReservation from Game g where g.isDeleted = 'false' and g.secondTeamReservation.reservationId = :reservationId")
    Optional<Reservation> findFirstTeamReservationBySecondTeamReservationId(@Param("reservationId") Long secondTeamReservationId);

}
