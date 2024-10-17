package team4.footwithme.team.service

import team4.footwithme.member.domain.Member
import team4.footwithme.team.service.request.TeamMemberServiceRequest
import team4.footwithme.team.service.response.TeamResponse

interface TeamMemberService {
    fun addTeamMembers(teamId: Long, request: TeamMemberServiceRequest?): List<TeamResponse>

    fun getTeamMembers(teamId: Long): List<TeamResponse>

    fun deleteTeamMemberByCreator(teamId: Long, teamMemberId: Long, member: Member?): Long

    fun deleteTeamMember(teamId: Long, member: Member?): Long?
}
