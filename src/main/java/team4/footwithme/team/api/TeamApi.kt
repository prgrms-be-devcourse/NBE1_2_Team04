package team4.footwithme.team.api

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.member.jwt.PrincipalDetails
import team4.footwithme.team.api.request.TeamCreateRequest
import team4.footwithme.team.api.request.TeamUpdateRequest
import team4.footwithme.team.service.TeamService
import team4.footwithme.team.service.response.TeamDefaultResponse
import team4.footwithme.team.service.response.TeamInfoResponse

@RestController
@RequestMapping("/api/v1/team")
class TeamApi(private val teamService: TeamService) {
    /**
     * 팀 생성
     */
    @PostMapping("/create")
    fun createTeam(
        @RequestBody request: TeamCreateRequest,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<TeamDefaultResponse?> {
        return ApiResponse.Companion.created<TeamDefaultResponse?>(
            teamService.createTeam(
                request.toServiceRequest(),
                principalDetails.member
            )
        )
    }

    /**
     * 팀 정보 조회
     */
    @GetMapping("/{teamId}/info")
    fun getTeamInfo(@PathVariable teamId: Long): ApiResponse<TeamInfoResponse?> {
        val teamInfoResponse = teamService.getTeamInfo(teamId)
        return ApiResponse.Companion.ok<TeamInfoResponse?>(teamInfoResponse)
    }

    /**
     * 팀 정보 수정
     */
    @PutMapping("/{teamId}/info")
    fun updateTeamInfo(
        @PathVariable teamId: Long,
        @RequestBody request: TeamUpdateRequest,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<TeamDefaultResponse?> {
        val teamUpdateResponse = teamService.updateTeamInfo(teamId, request.toServiceRequest(), principalDetails.member)
        return ApiResponse.Companion.ok<TeamDefaultResponse?>(teamUpdateResponse)
    }

    /**
     * 팀 삭제
     */
    @DeleteMapping("/{teamId}")
    fun deleteTeam(
        @PathVariable teamId: Long,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<Long?> {
        //요청받은 id리턴
        val deletedId = teamService.deleteTeam(teamId, principalDetails.member)
        return ApiResponse.Companion.ok<Long?>(deletedId)
    }
}
