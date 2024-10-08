package team4.footwithme.team.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.team.api.request.TeamCreateRequest;
import team4.footwithme.team.api.request.TeamUpdateRequest;
import team4.footwithme.team.service.TeamService;
import team4.footwithme.team.service.response.TeamDefaultResponse;
import team4.footwithme.team.service.response.TeamInfoResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/team")
public class TeamApi {

    private final TeamService teamService;

    /**
     * 팀 생성
     */
    @PostMapping("/create")
    public ApiResponse<TeamDefaultResponse> createTeam(@RequestBody TeamCreateRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.created(teamService.createTeam(request.toServiceRequest(), principalDetails.getMember()));
    }

    /**
     * 팀 정보 조회
     */
    @GetMapping("/{teamId}/info")
    public ApiResponse<TeamInfoResponse> getTeamInfo(@PathVariable Long teamId) {
        TeamInfoResponse teamInfoResponse = teamService.getTeamInfo(teamId);
        return ApiResponse.ok(teamInfoResponse);
    }

    /**
     * 팀 정보 수정
     */
    @PutMapping("/{teamId}/info")
    public ApiResponse<TeamDefaultResponse> updateTeamInfo(@PathVariable Long teamId, @RequestBody TeamUpdateRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        TeamDefaultResponse teamUpdateResponse = teamService.updateTeamInfo(teamId, request.toServiceRequest(), principalDetails.getMember());
        return ApiResponse.ok(teamUpdateResponse);
    }

    /**
     * 팀 삭제
     */
    @DeleteMapping("/{teamId}")
    public ApiResponse<Long> deleteTeam(@PathVariable Long teamId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        //요청받은 id리턴
        Long deletedId = teamService.deleteTeam(teamId, principalDetails.getMember());
        return ApiResponse.ok(deletedId);
    }
}
