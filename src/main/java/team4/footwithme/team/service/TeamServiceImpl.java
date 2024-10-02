package team4.footwithme.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.team.domain.*;
import team4.footwithme.team.repository.TeamMemberRepository;
import team4.footwithme.team.repository.TeamRateRepository;
import team4.footwithme.team.service.request.TeamDefaultServiceRequest;
import team4.footwithme.team.service.response.TeamInfoResponse;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.team.service.response.TeamDefaultResponse;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamRateRepository teamRateRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public TeamDefaultResponse createTeam(TeamDefaultServiceRequest dto, Member member) {
        /**
         * # 1.
         * chatRoomId는 일단 가짜 id 넣어놓고,
         * 채팅방 생기면 해당 id 적용
         */
        Long stadiumId = null;
        Long chatRoomId = 100000L;

        //TotalRecord 초기값으로 생성
        TotalRecord totalRecord = TotalRecord.builder().build();

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

        //TODO :: 현재 유저 팀장으로 TeamMember에 저장
        teamMemberRepository.save(TeamMember.create(createdTeam, member, TeamMemberRole.CREATOR));

        return TeamDefaultResponse.from(createdTeam);
    }


    @Override
    @Transactional(readOnly = true)
    public TeamInfoResponse getTeamInfo(Long teamId) {

        //팀 정보
        Team teamEntity = findTeamByIdOrThrowException(teamId);

        //팀 평가 ->  List
        List<TeamRate> teamRates = teamRateRepository.findEvaluationsByTeam(teamEntity);
        List<String> evaluations = new ArrayList<>();

        for(TeamRate teamRate : teamRates){
            evaluations.add(teamRate.getEvaluation());
        }

        Long maleCount = teamRepository.countMaleByMemberId();
        Long femaleCount = teamRepository.countFemaleByMemberId();

        return TeamInfoResponse.of(
            teamEntity,
            evaluations,
            maleCount,
            femaleCount
        );
    }

    @Override
    @Transactional
    public TeamDefaultResponse updateTeamInfo(Long teamId, TeamDefaultServiceRequest dto, Member member) {
        //변경할 팀 id로 검색
        Team teamEntity = findTeamByIdOrThrowException(teamId);
        //현재 유저 정보 검색
        TeamMember teamMember = findTeamMemberByIdOrThrowException(teamId, member.getMemberId());
        //권한 정보
        if(checkAuthority(teamId, teamMember) == false) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        //entity에 수정된 값 적용
        if (dto.name() != null) {
            teamEntity.updateName(dto.name());
        }
        if (dto.description() != null) {
            teamEntity.updateDescription(dto.description());
        }
        if (dto.location() != null) {
            teamEntity.updateLocation(dto.location());
        }

        //바뀐 Team값 반환
        return TeamDefaultResponse.from(teamEntity);

    }

    @Override
    @Transactional
    public Long deleteTeam(Long teamId, Member member) {
        //삭제할 팀 탐색
        Team teamEntity = findTeamByIdOrThrowException(teamId);
        //현재 유저 정보 검색
        TeamMember teamMember = findTeamMemberByIdOrThrowException(teamId, member.getMemberId());
        //권한 정보
        if(checkAuthority(teamId, teamMember) == false) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        teamRepository.delete(teamEntity);
        return teamId;
    }


    public Team findTeamByIdOrThrowException(long id){
        Team team = teamRepository.findByTeamId(id);
        if(team == null) {
            throw new IllegalArgumentException("해당 팀이 존재하지 않습니다.");
        }
        return team;
    }

    public TeamMember findTeamMemberByIdOrThrowException(Long teamId, Long memberId){
        TeamMember teamMember = teamMemberRepository.findByTeamIdAndMemberId(teamId, memberId);

        if(teamMember == null || teamMember.getIsDeleted().equals(IsDeleted.TRUE)) {
            throw new IllegalArgumentException("존재하지 않는 팀원입니다.");
        }
        return teamMember;
    }

    public boolean checkAuthority(Long teamId, TeamMember teamMember){
        if(teamMember.getTeam().getTeamId() == teamId && teamMember.getRole() == TeamMemberRole.CREATOR) {
            return true;
        }
        return false;
    }

}
