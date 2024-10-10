package team4.footwithme.resevation.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team4.footwithme.global.repository.CustomGlobalRepository;
import team4.footwithme.resevation.domain.Game;
import team4.footwithme.resevation.domain.GameStatus;
import team4.footwithme.resevation.domain.Reservation;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>, CustomGlobalRepository {
    @Query("SELECT g FROM Game g WHERE g.isDeleted = 'FALSE' AND g.secondTeamReservation = :reservation AND g.gameStatus = :status")
    Slice<Game> findBySecondReservationAndStatus(
        @Param("reservation") Reservation reservation,
        @Param("status") GameStatus status,
        PageRequest pageRequest);

    @Query("SELECT g FROM Game g WHERE g.isDeleted = 'false' AND g.gameId = :id")
    Optional<Game> findActiveById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Game g SET g.isDeleted = 'TRUE' WHERE g.secondTeamReservation = :reservation")
    void softDeleteBySecondTeamReservation(@Param("reservation") Reservation reservation);

    @Query("select g.firstTeamReservation from Game g where g.isDeleted = 'false' and g.secondTeamReservation.reservationId = :reservationId")
    Optional<Reservation> findFirstTeamReservationBySecondTeamReservationId(@Param("reservationId") Long secondTeamReservationId);

    @Query("select g from Game g where g.isDeleted = 'false' and g.firstTeamReservation.reservationId = :reservationId")
    List<Game> findAllByReservationId(Long reservationId);

}
