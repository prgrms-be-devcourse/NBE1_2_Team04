package team4.footwithme.team.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.team.api.request.TeamCreateRequest;
import team4.footwithme.team.api.request.TeamUpdateRequest;
import team4.footwithme.team.service.response.TeamInfoResponse;
import team4.footwithme.team.service.TeamService;
import team4.footwithme.team.service.response.TeamDefaultResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/team")
public class TeamApi {

    private final TeamService teamService;

    /**
     * 팀 생성
     */
    @PostMapping("/create")
    public ApiResponse<TeamDefaultResponse> createTeam(@RequestBody TeamCreateRequest request) {
        return ApiResponse.created(teamService.createTeam(request.toServiceRequest()));
    }

    /**
     * 경기 일정(날짜만) 조회
     */
    @GetMapping("/{teamId}")
    public ApiResponse<?> getTeamReservationInfo(@PathVariable Long teamId){
        return null;
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
    public ApiResponse<TeamDefaultResponse> updateTeamInfo(@PathVariable Long teamId, @RequestBody TeamUpdateRequest request) {
        TeamDefaultResponse teamUpdateResponse = teamService.updateTeamInfo(teamId, request.toServiceRequest());
        return ApiResponse.ok(teamUpdateResponse);
    }

    /**
     * 팀 삭제
     */
    @DeleteMapping("/{teamId}")
    public ApiResponse<Long> deleteTeam(@PathVariable Long teamId) {
        //요청받은 id리턴
        Long deletedId = teamService.deleteTeam(teamId);
        return ApiResponse.ok(deletedId);
    }
}
