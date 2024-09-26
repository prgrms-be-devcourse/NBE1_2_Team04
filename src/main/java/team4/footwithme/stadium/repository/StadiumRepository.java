package team4.footwithme.stadium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team4.footwithme.stadium.domain.Stadium;

import java.util.List;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium,Long> {

    List<Stadium> findByNameContainingIgnoreCase(String query);
    List<Stadium> findByAddressContainingIgnoreCase(String address);

    @Query(value = "SELECT * FROM stadium WHERE ST_Distance_Sphere(position, ST_GeomFromText(:point, 4326)) <= :distance * 1000", nativeQuery = true)
    List<Stadium> findStadiumsByLocation(@Param("point") String point, @Param("distance") Double distance);
}
