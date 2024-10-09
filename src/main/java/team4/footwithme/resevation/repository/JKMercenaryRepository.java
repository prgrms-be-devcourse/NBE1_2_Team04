package team4.footwithme.resevation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team4.footwithme.resevation.domain.Mercenary;

import java.util.List;
import java.util.Optional;

@Repository
public interface JKMercenaryRepository extends JpaRepository<Mercenary, Long> {
    @Query("select mer from Mercenary mer where mer.isDeleted = 'false' and mer.reservation.reservationId = :reservationId")
    List<Mercenary> findAllMercenaryByReservationId(Long reservationId);
}
