package team4.footwithme.team.service;

import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.team.api.request.TeamApiRequest;
import team4.footwithme.team.api.response.TeamAddResponse;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TotalRecord;
import team4.footwithme.team.service.response.TeamCreatedResponse;

public interface TeamService {
    /**
     * 팀 생성
     */
    TeamCreatedResponse createTeam(TeamAddResponse dto);
    /**
     * 해당팀 정보 조회
     * 팀아이디, 팀이름, 팀설명, 전적, 활동 지역
     */
    Team getTeamInfo(Long teamId);

}
