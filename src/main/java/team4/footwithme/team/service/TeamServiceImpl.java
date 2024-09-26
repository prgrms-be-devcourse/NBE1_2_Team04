package team4.footwithme.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.team.api.response.TeamAddResponse;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TotalRecord;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.team.service.response.TeamCreatedResponse;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public TeamCreatedResponse createTeam(TeamAddResponse dto) {
        /**
         * # 1.
         * chatRoomId는 일단 가짜 id 넣어놓고,
         * 채팅방 생기면 해당 id 적용
         */
        Long stadiumId = null;
        Long chatRoomId = 100000L;
        //TotalRecord 초반 셋팅: count 0
        TotalRecord totalRecord = TotalRecord.builder()
                .winCount(0)
                .drawCount(0)
                .loseCount(0)
                .build();

        //dto -> entity
        Team entity = Team.create(
                stadiumId,
                chatRoomId,
                dto.name(),
                dto.description(),
                totalRecord.getWinCount(),
                totalRecord.getDrawCount(),
                totalRecord.getLoseCount(),
                dto.location()
        );
        Team createdTeam = teamRepository.save(entity);

        return new TeamCreatedResponse(
                createdTeam.getTeamId(),
                createdTeam.getStadiumId(),
                createdTeam.getChatRoomId(),
                createdTeam.getName(),
                createdTeam.getDescription(),
                createdTeam.getTotalRecord().getWinCount(),
                createdTeam.getTotalRecord().getDrawCount(),
                createdTeam.getTotalRecord().getLoseCount(),
                createdTeam.getLocation()
        );
    }

    @Override
    public Team getTeamInfo(Long teamId) {
        /**
         * 팀이름
         * 팀지역
         * 팀전적
         * 팀 평가
         * 팀 평균나이
         * 팀 성별
         */
        //teamRepository.findById(teamId).stream().;
        return null;
    }
}
