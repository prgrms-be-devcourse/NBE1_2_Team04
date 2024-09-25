package team4.footwithme.team.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.team.api.response.TeamAddResponse;
import team4.footwithme.team.service.TeamService;
import team4.footwithme.team.service.response.TeamCreatedResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/team")
public class TeamApi {

    private final TeamService teamService;

    @PostMapping("/create")
    public ApiResponse<TeamCreatedResponse> createTeam(@RequestBody TeamAddResponse dto) {
        Stadium stadium = null;
        TeamCreatedResponse teamCreatedResponse = teamService.createTeam(stadium,dto);
        return ApiResponse.created(teamCreatedResponse);
    }
}
