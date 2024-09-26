package team4.footwithme.team.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.team.api.request.TeamAddResquest;
import team4.footwithme.team.service.response.TeamInfoResponse;
import team4.footwithme.team.service.TeamService;
import team4.footwithme.team.service.response.TeamCreatedResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/team")
public class TeamApi {

    private final TeamService teamService;

    @PostMapping("/create")
    public ApiResponse<TeamCreatedResponse> createTeam(@RequestBody TeamAddResquest dto) {
        TeamCreatedResponse teamCreatedResponse = teamService.createTeam(dto);
        return ApiResponse.created(teamCreatedResponse);
    }

    @GetMapping("/{teamId}")
    public ApiResponse<?> getTeamReservaionInfo(@PathVariable Long teamId){
        /**
         * 경기 일정(날짜만) 조회
         */
        return null;
    }

    @GetMapping("/{teamId}/info")
    public ApiResponse<TeamInfoResponse> getTeamInfo(@PathVariable Long teamId) {
        /**
         * 팀 정보 조회
         */
        TeamInfoResponse teamInfoResponse = teamService.getTeamInfo(teamId);
        return ApiResponse.ok(teamInfoResponse);
    }
}
