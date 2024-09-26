package team4.footwithme.team.service;

import team4.footwithme.team.api.request.TeamAddResquest;
import team4.footwithme.team.service.response.TeamInfoResponse;
import team4.footwithme.team.service.response.TeamCreatedResponse;

public interface TeamService {
    /**
     * 팀 생성
     */
    TeamCreatedResponse createTeam(TeamAddResquest dto);
    /**
     * 해당팀 정보 조회
     * 팀아이디, 팀이름, 팀설명, 전적, 활동 지역
     */
    TeamInfoResponse getTeamInfo(Long teamId);

}
