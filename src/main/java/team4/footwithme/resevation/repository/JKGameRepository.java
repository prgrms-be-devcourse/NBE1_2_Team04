package team4.footwithme.resevation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team4.footwithme.resevation.domain.Game;

import java.util.List;

@Repository
public interface JKGameRepository extends JpaRepository<Game, Long> {
    @Query("select g from Game g where g.isDeleted = 'false' and g.firstTeamReservation.reservationId = :reservationId")
    List<Game> findAllByReservationId(Long reservationId);
}
