package team4.footwithme.stadium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team4.footwithme.stadium.domain.Court;

import java.util.List;

@Repository
public interface CourtRepository extends JpaRepository<Court,Long> {

    List<Court> findByStadium_StadiumId(@Param("stadiumId") Long stadiumId);
}
