package team4.footwithme.resevation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team4.footwithme.resevation.domain.Reservation;

import java.util.Optional;

public interface MWReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r where r.isDeleted = 'false' and r.reservationId = :id")
    Optional<Reservation> findByReservationId(@Param("id") Long reservationId);
}
