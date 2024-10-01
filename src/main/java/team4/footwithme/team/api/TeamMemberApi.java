package team4.footwithme.team.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.team.api.request.TeamMemberRequest;
import team4.footwithme.team.service.TeamMemberService;
import team4.footwithme.team.service.response.TeamResponse;

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
    public ApiResponse<List<TeamResponse>> addTeamMembers(@PathVariable Long teamId, @RequestBody TeamMemberRequest request){
        List<TeamResponse> members = teamMemberService.addTeamMembers(teamId, request.toServiceRequest());
        return ApiResponse.ok(members);
    }

    /**
     * 팀 멤버 전체 조회
     */
    @GetMapping("/{teamId}/members")
    public ApiResponse<List<TeamResponse>> getTeamMembers(@PathVariable Long teamId){
        List<TeamResponse> members = teamMemberService.getTeamMembers(teamId);
        return ApiResponse.ok(members);
    }
    /**
     * 팀 멤버 수정 - role
     */

    /**
     * 팀 멤버 삭제
     */
    @DeleteMapping("/{teamMemberId}")
    public ApiResponse<Long> deleteTeamMember(@PathVariable Long teamMemberId){
        teamMemberService.deleteTeamMembers(teamMemberId);
        return ApiResponse.ok(teamMemberId);
    }


}
