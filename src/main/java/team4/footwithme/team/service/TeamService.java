package team4.footwithme.team.service;

import team4.footwithme.team.service.request.TeamDefaultServiceRequest;
import team4.footwithme.team.service.response.TeamInfoResponse;
import team4.footwithme.team.service.response.TeamDefaultResponse;

public interface TeamService {
    /**
     * 팀 생성
     */
    TeamDefaultResponse createTeam(TeamDefaultServiceRequest dto);
    /**
     * 해당팀 정보 조회
     * 팀아이디, 팀이름, 팀설명, 전적, 활동 지역
     */
    TeamInfoResponse getTeamInfo(Long teamId);
    /**
     * 팀 정보 수정
     * 수정: 팀 이름, 팀 설명, 활동지역
     */
    TeamDefaultResponse updateTeamInfo(Long teamId, TeamDefaultServiceRequest request);

    /**
     * 팀 삭제
     */
    Long deleteTeam(Long teamId);

}
