package team4.footwithme.resevation.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team4.footwithme.global.repository.CustomGlobalRepository;
import team4.footwithme.resevation.domain.Reservation;
import team4.footwithme.resevation.domain.ReservationStatus;
import team4.footwithme.stadium.domain.Court;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, CustomGlobalRepository {

    @Query("SELECT r FROM Reservation r WHERE r.isDeleted = 'false' AND r.reservationId = :id")
    Optional<Reservation> findActiveById(@Param("id") Long id);

    @Query("SELECT r FROM Reservation r WHERE r.isDeleted = 'FALSE' AND r.matchDate = :matchDate AND r.court = :court AND r.reservationStatus = :reservationStatus AND r.reservationId != :id")
    Slice<Reservation> findByMatchDateAndCourtAndReservationStatus(
            @Param("id") Long id,
            @Param("matchDate") LocalDateTime matchDate,
            @Param("court") Court court,
            @Param("reservationStatus") ReservationStatus reservationStatus,
            PageRequest pageRequest);

    @Query("select r from Reservation r where r.isDeleted = 'false' and r.reservationId = :id")
    Optional<Reservation> findByReservationId(@Param("id") Long reservationId);

    @Query("select r from Reservation r where r.isDeleted = 'false' and r.team.teamId = :teamId")
    List<Reservation> findByTeamTeamId(@Param("teamId")Long teamId);
}
