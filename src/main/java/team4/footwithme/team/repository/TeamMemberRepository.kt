package team4.footwithme.team.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import team4.footwithme.team.domain.Team
import team4.footwithme.team.domain.TeamMember
import java.util.*

@Repository
interface TeamMemberRepository : JpaRepository<TeamMember?, Long?>, CustomTeamMemberRepository {
    @Query("select tm from TeamMember tm where tm.isDeleted = 'false' and tm.team = :team")
    fun findTeamMembersByTeam(@Param("team") team: Team?): List<TeamMember?>?

    @Query("select tm from TeamMember tm where tm.isDeleted = 'false' and tm.teamMemberId = :id")
    fun findByTeamMemberId(@Param("id") teamMemberId: Long?): Optional<TeamMember>

    @Query("SELECT tm FROM TeamMember tm JOIN FETCH tm.member WHERE tm.isDeleted = 'false' and tm.teamMemberId = :teamMemberId")
    fun findTeamMemberWithMemberById(@Param("teamMemberId") teamMemberId: Long?): Optional<TeamMember?>?
}
