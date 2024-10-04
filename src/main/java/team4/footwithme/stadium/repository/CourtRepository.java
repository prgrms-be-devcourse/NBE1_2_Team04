package team4.footwithme.stadium.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team4.footwithme.global.repository.CustomGlobalRepository;
import team4.footwithme.stadium.domain.Court;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long>, CustomGlobalRepository {

    @Query("SELECT c FROM Court c WHERE c.isDeleted = 'false' AND c.stadium.stadiumId = :stadiumId")
    Slice<Court> findByStadium_StadiumId(@Param("stadiumId") Long stadiumId, Pageable pageable);

    @Query("SELECT c FROM Court c WHERE c.isDeleted = 'false'")
    Slice<Court> findAllActive(Pageable pageable);

    @Query("SELECT c FROM Court c WHERE c.isDeleted = 'false' AND c.courtId = :id")
    Optional<Court> findByCourtId(@Param("id") Long id);

    @Query("SELECT c FROM Court c WHERE c.isDeleted = 'false' AND c.courtId = :id")
    Optional<Court> findActiveById(@Param("id") Long id);

    @Query("SELECT c FROM Court c WHERE c.isDeleted = 'false' AND c.stadium.stadiumId = :id")
    List<Court> findActiveByStadiumId(@Param("id") Long id);
}
