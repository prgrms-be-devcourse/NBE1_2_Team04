package team4.footwithme.team.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team4.footwithme.chat.service.event.TeamMemberJoinEvent
import team4.footwithme.chat.service.event.TeamMemberLeaveEvent
import team4.footwithme.chat.service.event.TeamMembersJoinEvent
import team4.footwithme.member.domain.Member
import team4.footwithme.member.repository.MemberRepository
import team4.footwithme.team.domain.Team
import team4.footwithme.team.domain.TeamMember
import team4.footwithme.team.domain.TeamMemberRole
import team4.footwithme.team.repository.TeamMemberRepository
import team4.footwithme.team.repository.TeamRepository
import team4.footwithme.team.service.request.TeamMemberServiceRequest
import team4.footwithme.team.service.response.TeamResponse

@Service
class TeamMemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val teamRepository: TeamRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val publisher: ApplicationEventPublisher
) : TeamMemberService {
    @Transactional
    override fun addTeamMembers(teamId: Long, request: TeamMemberServiceRequest?): List<TeamResponse> {
        //팀원 추가할 팀 찾기
        val team = findTeamByIdOrThrowException(teamId)

        //return할 DTO
        val teamMembers: MutableList<TeamResponse> = ArrayList()

        //채팅방에 초대할 TeamMember List
        val teamMemberList: MutableList<TeamMember> = ArrayList()

        //member 추가
        for (email in request!!.emails!!) {
            val member = memberRepository.findByEmail(email).orElse(null) ?: continue

            var teamMember = teamMemberRepository.findByTeamIdAndMemberId(teamId, member.memberId).orElse(null)
            //해당 멤버가 팀에 이미 존재 할 경우
            if (teamMember != null) {
                continue
            }

            teamMember = TeamMember.Companion.createMember(team, member)

            teamMembers.add(TeamResponse.Companion.of(teamMemberRepository.save<TeamMember>(teamMember)))

            teamMemberList.add(teamMember)
        }
        // 팀 멤버 채팅방 초대
        if (teamMemberList.isEmpty()) {
            return teamMembers
        }
        if (teamMemberList.size == 1) {
            publisher.publishEvent(TeamMemberJoinEvent(teamMemberList[0].member, team.teamId))
        } else {
            publisher.publishEvent(TeamMembersJoinEvent(teamMemberList, team.teamId))
        }

        return teamMembers
    }

    @Transactional(readOnly = true)
    override fun getTeamMembers(teamId: Long): List<TeamResponse> {
        //팀 찾기
        val team = findTeamByIdOrThrowException(teamId)

        val teamMembers = teamMemberRepository.findTeamMembersByTeam(team)
        val membersInfo: MutableList<TeamResponse> = ArrayList()

        for (teamMember in teamMembers!!) {
            membersInfo.add(TeamResponse.Companion.of(teamMember))
        }
        return membersInfo
    }

    //팀 탈퇴_팀장
    @Transactional
    override fun deleteTeamMemberByCreator(teamId: Long, teamMemberId: Long, member: Member?): Long {
        //삭제할 팀 멤버 찾기
        val teamMember = findTeamMemberByIdOrThrowException(teamMemberId)
        //현재 유저 정보
        val Creator = findByTeamIdAndMemberIdOrThrowException(teamId, member!!.memberId!!)

        require(Creator.role == TeamMemberRole.CREATOR) { "삭제 권한이 없습니다" }

        teamMemberRepository.delete(teamMember)
        //팀 멤버 삭제시 해당 멤버 채팅방 퇴장 이벤트 처리
        publisher.publishEvent(TeamMemberLeaveEvent(teamMember.member, teamMember.team?.teamId))
        return teamMemberId
    }

    //팀 탈퇴_본인
    @Transactional
    override fun deleteTeamMember(teamId: Long, member: Member?): Long? {
        val teamMember = findByTeamIdAndMemberIdOrThrowException(teamId, member!!.memberId!!)
        teamMemberRepository.delete(teamMember)
        //팀 멤버 삭제시 해당 멤버 채팅방 퇴장 이벤트 처리
        publisher.publishEvent(TeamMemberLeaveEvent(teamMember.member, teamMember.team?.teamId))
        return teamMember.teamMemberId
    }


    fun findTeamByIdOrThrowException(id: Long): Team {
        val team = teamRepository.findByTeamId(id)
            .orElseThrow { IllegalArgumentException("해당 팀이 존재하지 않습니다.") }

        return team
    }

    fun findTeamMemberByIdOrThrowException(id: Long): TeamMember {
        val teamMember = teamMemberRepository.findByTeamMemberId(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 팀원입니다.") }
        return teamMember
    }

    fun findByTeamIdAndMemberIdOrThrowException(teamId: Long, memberId: Long): TeamMember {
        val teamMember = teamMemberRepository.findByTeamIdAndMemberId(teamId, memberId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 팀원입니다.") }
        return teamMember
    }
}
