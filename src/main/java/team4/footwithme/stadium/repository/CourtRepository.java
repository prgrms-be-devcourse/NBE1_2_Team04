package team4.footwithme.stadium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team4.footwithme.stadium.domain.Court;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

    @Query("SELECT c FROM Court c WHERE c.isDeleted = 'false' AND c.stadium.stadiumId = :stadiumId")
    List<Court> findByStadium_StadiumId(@Param("stadiumId") Long stadiumId);

    @Query("SELECT c FROM Court c WHERE c.isDeleted = 'false'")
    List<Court> findAllActive();

    @Query("SELECT c FROM Court c WHERE c.isDeleted = 'false' AND c.courtId = :id")
    Optional<Court> findByCourtId(@Param("id") Long id);
}
