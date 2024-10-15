package team4.footwithme.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.chat.service.event.TeamDeletedEvent;
import team4.footwithme.chat.service.event.TeamMemberJoinEvent;
import team4.footwithme.chat.service.event.TeamPublishedEvent;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.team.domain.*;
import team4.footwithme.team.repository.TeamMemberRepository;
import team4.footwithme.team.repository.TeamRateRepository;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.team.service.request.TeamDefaultServiceRequest;
import team4.footwithme.team.service.response.TeamDefaultResponse;
import team4.footwithme.team.service.response.TeamInfoResponse;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamRateRepository teamRateRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;

    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public TeamDefaultResponse createTeam(TeamDefaultServiceRequest dto, Member member) {
        /**
         * # 1.
         * chatRoomId는 일단 가짜 id 넣어놓고,
         * 채팅방 생기면 해당 id 적용
         */
        Long stadiumId = null;

        //TotalRecord 초기값으로 생성
        TotalRecord totalRecord = TotalRecord.builder().build();

        //dto -> entity
        Team entity = Team.create(
            stadiumId,
            dto.name(),
            dto.description(),
            totalRecord.getWinCount(),
            totalRecord.getDrawCount(),
            totalRecord.getLoseCount(),
            dto.location()
        );
        Team createdTeam = teamRepository.save(entity);

        //채팅방 생성 이벤트 실행
        publisher.publishEvent(new TeamPublishedEvent(createdTeam.getName(), createdTeam.getTeamId()));

        teamMemberRepository.save(TeamMember.createCreator(createdTeam, member));

        publisher.publishEvent(new TeamMemberJoinEvent(member, createdTeam.getTeamId()));

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

        for (TeamRate teamRate : teamRates) {
            evaluations.add(teamRate.getEvaluation());
        }

        Long maleCount = teamRepository.countMaleByMemberId(teamId);
        Long femaleCount = teamRepository.countFemaleByMemberId(teamId);

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
        checkAuthority(teamId, teamMember);

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
        checkAuthority(teamId, teamMember);

        teamRepository.delete(teamEntity);
        // 채팅방 삭제 이벤트 실행
        publisher.publishEvent(new TeamDeletedEvent(teamId));
        return teamId;
    }


    public Team findTeamByIdOrThrowException(long id) {
        Team team = teamRepository.findByTeamId(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 팀이 존재하지 않습니다."));
        return team;
    }

    public TeamMember findTeamMemberByIdOrThrowException(Long teamId, Long memberId) {
        TeamMember teamMember = teamMemberRepository.findByTeamIdAndMemberId(teamId, memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀원입니다."));
        return teamMember;
    }

    public void checkAuthority(Long teamId, TeamMember teamMember) {
        if (teamMember.getTeam().getTeamId() != teamId || teamMember.getRole() != TeamMemberRole.CREATOR) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
    }

}
