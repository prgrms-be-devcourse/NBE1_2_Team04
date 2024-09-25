package team4.footwithme.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team4.footwithme.team.domain.TeamRate;

@Repository
public interface TeamRateRepository extends JpaRepository<TeamRate, Long> {
}
