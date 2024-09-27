package team4.footwithme.team.service;

import team4.footwithme.team.service.request.TeamCreateServiceRequest;
import team4.footwithme.team.service.response.TeamInfoResponse;
import team4.footwithme.team.service.response.TeamCreatedResponse;

public interface TeamService {
    /**
     * 팀 생성
     */
    TeamCreatedResponse createTeam(TeamCreateServiceRequest dto);
    /**
     * 해당팀 정보 조회
     * 팀아이디, 팀이름, 팀설명, 전적, 활동 지역
     */
    TeamInfoResponse getTeamInfo(Long teamId);

}
