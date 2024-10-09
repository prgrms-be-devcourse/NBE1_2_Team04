package team4.footwithme.resevation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import team4.footwithme.resevation.domain.Reservation;

import java.util.List;

@Repository
public interface YYReservationRepository extends JpaRepository<Reservation, Long>{
    @Query("select r from Reservation r where r.isDeleted = 'false' and r.team.teamId = :teamId")
    List<Reservation> findByTeamTeamId(@Param("teamId")Long teamId);
}
