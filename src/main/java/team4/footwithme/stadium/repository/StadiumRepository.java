package team4.footwithme.stadium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team4.footwithme.stadium.domain.Stadium;

import java.util.List;
import java.util.Optional;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium,Long>, CustomStadiumRepository {


    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false' AND s.stadiumId = :id")
    Optional<Stadium> findByStadiumId(@Param("id") Long id);

    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false'")
    List<Stadium> findAllActiveStadiums();

    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false' AND LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Stadium> findByNameContainingIgnoreCase(@Param("query") String query);

    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false' AND LOWER(s.address) LIKE LOWER(CONCAT('%', :address, '%'))")
    List<Stadium> findByAddressContainingIgnoreCase(@Param("address") String address);







//    @Query(value = "SELECT * FROM stadium WHERE ST_Distance_Sphere(position, ST_GeomFromText(:point, 4326)) <= :distance * 1000", nativeQuery = true)
//    List<Stadium> findStadiumsByLocation(@Param("point") String point, @Param("distance") Double distance);
}
