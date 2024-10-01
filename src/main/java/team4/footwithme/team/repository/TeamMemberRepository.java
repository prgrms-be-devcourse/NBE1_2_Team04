package team4.footwithme.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team4.footwithme.member.domain.Member;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamMember;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    @Query("select tm from TeamMember tm where tm.isDeleted = 'false'")
    List<TeamMember> findTeamMembersByTeam(Team team);

    @Query("select tm from TeamMember tm where tm.isDeleted = 'false' and tm.teamMemberId = :id")
    TeamMember findByTeamMemberId(@Param("id")Long teamMemberId);
}
