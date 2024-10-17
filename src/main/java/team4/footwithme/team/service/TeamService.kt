package team4.footwithme.team.service

import team4.footwithme.member.domain.Member
import team4.footwithme.team.service.request.TeamDefaultServiceRequest
import team4.footwithme.team.service.response.TeamDefaultResponse
import team4.footwithme.team.service.response.TeamInfoResponse

interface TeamService {
    /**
     * 팀 생성
     */
    fun createTeam(dto: TeamDefaultServiceRequest?, member: Member?): TeamDefaultResponse

    /**
     * 해당팀 정보 조회
     * 팀아이디, 팀이름, 팀설명, 전적, 활동 지역
     */
    fun getTeamInfo(teamId: Long): TeamInfoResponse

    /**
     * 팀 정보 수정
     * 수정: 팀 이름, 팀 설명, 활동지역
     */
    fun updateTeamInfo(teamId: Long, request: TeamDefaultServiceRequest?, member: Member?): TeamDefaultResponse

    /**
     * 팀 삭제
     */
    fun deleteTeam(teamId: Long, member: Member?): Long
}
