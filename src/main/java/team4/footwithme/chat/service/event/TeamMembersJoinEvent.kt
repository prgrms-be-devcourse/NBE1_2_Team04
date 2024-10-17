package team4.footwithme.chat.service.event

import team4.footwithme.team.domain.TeamMember

@JvmRecord
data class TeamMembersJoinEvent(
    val members: List<TeamMember>,
    val teamId: Long?
)
