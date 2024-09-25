package team4.footwithme.resevation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team4.footwithme.resevation.domain.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

}
