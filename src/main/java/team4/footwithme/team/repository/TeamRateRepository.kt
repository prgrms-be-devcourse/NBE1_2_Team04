package team4.footwithme.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamRate;

import java.util.List;

@Repository
public interface TeamRateRepository extends JpaRepository<TeamRate, Long> {
    List<TeamRate> findEvaluationsByTeam(Team team);
}
