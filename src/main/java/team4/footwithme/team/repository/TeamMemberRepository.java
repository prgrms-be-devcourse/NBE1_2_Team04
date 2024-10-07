package team4.footwithme.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamMember;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>, CustomTeamMemberRepository{
    @Query("select tm from TeamMember tm where tm.isDeleted = 'false' and tm.team = :team")
    List<TeamMember> findTeamMembersByTeam(@Param("team")Team team);

    @Query("select tm from TeamMember tm where tm.isDeleted = 'false' and tm.teamMemberId = :id")
    Optional<TeamMember> findByTeamMemberId(@Param("id")Long teamMemberId);

    @Query("SELECT tm FROM TeamMember tm JOIN FETCH tm.member WHERE tm.isDeleted = 'false' and tm.teamMemberId = :teamMemberId")
    Optional<TeamMember> findTeamMemberWithMemberById(@Param("teamMemberId") Long teamMemberId);
}
