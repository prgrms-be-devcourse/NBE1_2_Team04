package team4.footwithme.team.repository

import team4.footwithme.team.domain.TeamMember
import java.util.*

interface CustomTeamMemberRepository {
    fun findByTeamIdAndMemberId(teamId: Long?, memberId: Long?): Optional<TeamMember>
}
