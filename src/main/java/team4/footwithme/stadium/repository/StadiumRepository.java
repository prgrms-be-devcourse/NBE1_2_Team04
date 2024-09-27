package team4.footwithme.stadium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team4.footwithme.stadium.domain.Stadium;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium,Long>, CustomStadiumRepository {
}
