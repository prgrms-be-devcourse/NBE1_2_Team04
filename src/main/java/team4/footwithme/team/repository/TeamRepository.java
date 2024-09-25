package team4.footwithme.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team4.footwithme.team.domain.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
