package team4.footwithme.chat.service.event

import team4.footwithme.member.domain.Member

@JvmRecord
data class TeamMemberLeaveEvent(
    val member: Member?,
    val teamId: Long?
)
