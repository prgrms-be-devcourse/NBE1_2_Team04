package team4.footwithme.team.service.response

import team4.footwithme.team.domain.TeamMember
import team4.footwithme.team.domain.TeamMemberRole

@JvmRecord
data class TeamResponse(val teamMemberId: Long?, val teamId: Long?, @JvmField val name: String?, @JvmField val role: TeamMemberRole?) {
    companion object {
        fun of(teamMember: TeamMember?): TeamResponse {
            return TeamResponse(
                teamMember!!.teamMemberId,
                teamMember.team?.teamId,
                teamMember.member?.name,
                teamMember.role
            )
        }
    }
}
