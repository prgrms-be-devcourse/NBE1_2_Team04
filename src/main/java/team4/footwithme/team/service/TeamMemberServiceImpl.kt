package team4.footwithme.team.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.chat.service.event.TeamMemberJoinEvent;
import team4.footwithme.chat.service.event.TeamMemberLeaveEvent;
import team4.footwithme.chat.service.event.TeamMembersJoinEvent;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamMember;
import team4.footwithme.team.domain.TeamMemberRole;
import team4.footwithme.team.repository.TeamMemberRepository;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.team.service.request.TeamMemberServiceRequest;
import team4.footwithme.team.service.response.TeamResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamMemberServiceImpl implements TeamMemberService {


    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final ApplicationEventPublisher publisher;

    public TeamMemberServiceImpl(MemberRepository memberRepository, TeamRepository teamRepository, TeamMemberRepository teamMemberRepository, ApplicationEventPublisher publisher) {
        this.memberRepository = memberRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public List<TeamResponse> addTeamMembers(Long teamId, TeamMemberServiceRequest request) {
        //팀원 추가할 팀 찾기
        Team team = findTeamByIdOrThrowException(teamId);

        //return할 DTO
        List<TeamResponse> teamMembers = new ArrayList<>();

        //채팅방에 초대할 TeamMember List
        List<TeamMember> teamMemberList = new ArrayList<>();

        //member 추가
        for (String email : request.emails()) {
            Member member = memberRepository.findByEmail(email).orElse(null);

            if (member == null) {
                continue;
            }

            TeamMember teamMember = teamMemberRepository.findByTeamIdAndMemberId(teamId, member.getMemberId()).orElse(null);
            //해당 멤버가 팀에 이미 존재 할 경우
            if (teamMember != null) {
                continue;
            }

            teamMember = TeamMember.createMember(team, member);

            teamMembers.add(TeamResponse.of(teamMemberRepository.save(teamMember)));

            teamMemberList.add(teamMember);
        }
        // 팀 멤버 채팅방 초대
        if (teamMemberList.isEmpty()) {
            return teamMembers;
        }
        if (teamMemberList.size() == 1) {
            publisher.publishEvent(new TeamMemberJoinEvent(teamMemberList.get(0).getMember(), team.getTeamId()));
        } else {
            publisher.publishEvent(new TeamMembersJoinEvent(teamMemberList, team.getTeamId()));
        }

        return teamMembers;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponse> getTeamMembers(Long teamId) {
        //팀 찾기
        Team team = findTeamByIdOrThrowException(teamId);

        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);
        List<TeamResponse> membersInfo = new ArrayList<>();

        for (TeamMember teamMember : teamMembers) {
            membersInfo.add(TeamResponse.of(teamMember));
        }
        return membersInfo;
    }

    //팀 탈퇴_팀장
    @Override
    @Transactional
    public Long deleteTeamMemberByCreator(Long teamId, Long teamMemberId, Member member) {
        //삭제할 팀 멤버 찾기
        TeamMember teamMember = findTeamMemberByIdOrThrowException(teamMemberId);
        //현재 유저 정보
        TeamMember Creator = findByTeamIdAndMemberIdOrThrowException(teamId, member.getMemberId());

        if (Creator.getRole() != TeamMemberRole.CREATOR) {
            throw new IllegalArgumentException("삭제 권한이 없습니다");
        }

        teamMemberRepository.delete(teamMember);
        //팀 멤버 삭제시 해당 멤버 채팅방 퇴장 이벤트 처리
        publisher.publishEvent(new TeamMemberLeaveEvent(teamMember.getMember(), teamMember.getTeam().getTeamId()));
        return teamMemberId;
    }

    //팀 탈퇴_본인
    @Override
    @Transactional
    public Long deleteTeamMember(Long teamId, Member member) {
        TeamMember teamMember = findByTeamIdAndMemberIdOrThrowException(teamId, member.getMemberId());
        teamMemberRepository.delete(teamMember);
        //팀 멤버 삭제시 해당 멤버 채팅방 퇴장 이벤트 처리
        publisher.publishEvent(new TeamMemberLeaveEvent(teamMember.getMember(), teamMember.getTeam().getTeamId()));
        return teamMember.getTeamMemberId();
    }


    public Team findTeamByIdOrThrowException(long id) {
        Team team = teamRepository.findByTeamId(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 팀이 존재하지 않습니다."));

        return team;
    }

    public TeamMember findTeamMemberByIdOrThrowException(long id) {
        TeamMember teamMember = teamMemberRepository.findByTeamMemberId(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀원입니다."));
        return teamMember;
    }

    public TeamMember findByTeamIdAndMemberIdOrThrowException(long teamId, long memberId) {
        TeamMember teamMember = teamMemberRepository.findByTeamIdAndMemberId(teamId, memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀원입니다."));
        return teamMember;
    }
}
