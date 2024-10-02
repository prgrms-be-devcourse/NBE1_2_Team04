package team4.footwithme.stadium.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team4.footwithme.global.repository.CustomGlobalRepository;
import team4.footwithme.stadium.domain.Stadium;

import java.util.Optional;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long>, CustomStadiumRepository, CustomGlobalRepository {


    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false' AND s.stadiumId = :id")
    Optional<Stadium> findByStadiumId(@Param("id") Long id);

    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false' AND s.stadiumId = :id")
    Optional<Stadium> findActiveById(@Param("id") Long id);

    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false'")
    Slice<Stadium> findAllActiveStadiums(Pageable pageable);

    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false' AND LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Slice<Stadium> findByNameContainingIgnoreCase(@Param("query") String query, Pageable pageable);

    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false' AND LOWER(s.address) LIKE LOWER(CONCAT('%', :address, '%'))")
    Slice<Stadium> findByAddressContainingIgnoreCase(@Param("address") String address, Pageable pageable);


//    @Query(value = "SELECT * FROM stadium WHERE ST_Distance_Sphere(position, ST_GeomFromText(:point, 4326)) <= :distance * 1000", nativeQuery = true)
//    List<Stadium> findStadiumsByLocation(@Param("point") String point, @Param("distance") Double distance);
}
