package team4.footwithme.team.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.team.api.request.TeamMemberRequest;
import team4.footwithme.team.service.TeamMemberService;
import team4.footwithme.team.service.response.TeamMemberInfoResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/team_member")
public class TeamMemberApi {
    private final TeamMemberService teamMemberService;

    /**
     * 팀 멤버 추가
     */
    @PostMapping("/{teamId}/members")
    public ApiResponse<List<TeamMemberInfoResponse>> addTeamMembers(@PathVariable Long teamId, @RequestBody TeamMemberRequest request){
        List<TeamMemberInfoResponse> members = teamMemberService.addTeamMembers(teamId, request.toServiceRequest());
        return ApiResponse.ok(members);
    }

    /**
     * 팀 멤버 전체 조회
     */

    /**
     * 팀 멤버 수정 -포지서
     */

    /**
     * 팀 멤버 삭제
     */
}
