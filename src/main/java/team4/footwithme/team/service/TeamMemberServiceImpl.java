package team4.footwithme.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@RequiredArgsConstructor
@Service
public class TeamMemberServiceImpl implements TeamMemberService{


    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Override
    @Transactional
    public List<TeamResponse> addTeamMembers(Long teamId, TeamMemberServiceRequest request) {
        //팀원 추가할 팀 찾기
        Team team = findTeamByIdOrThrowException(teamId);

        //return할 DTO
        List<TeamResponse> teamMembers = new ArrayList<>();

        //member 추가
        for(String email : request.emails()){
            Member member = memberRepository.findByEmail(email).orElse(null);

            if(member == null){
                continue;
            }

            TeamMember teamMember =  teamMemberRepository.save(TeamMember.create(team, member, TeamMemberRole.MEMBER));

            teamMembers.add(TeamResponse.of(teamMember));
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

        for(TeamMember teamMember : teamMembers){
            membersInfo.add(TeamResponse.of(teamMember));
        }
        return membersInfo;
    }

    @Override
    @Transactional
    public Long deleteTeamMembers(Long teamMemberId) {
        //팀 멤버 찾기
        TeamMember teamMember = findTeamMemberByIdOrThrowException(teamMemberId);
        teamMemberRepository.delete(teamMember);
        return teamMemberId;
    }


    public Team findTeamByIdOrThrowException(long id){
        Team team = teamRepository.findByTeamId(id);
        if(team == null) {
            throw new IllegalArgumentException("해당 팀이 존재하지 않습니다.");
        }
        return team;
    }

    public TeamMember findTeamMemberByIdOrThrowException(long id){
        TeamMember teamMember = teamMemberRepository.findByTeamMemberId(id);
        if(teamMember == null) {
            throw new IllegalArgumentException("존재하지 않는 팀원입니다.");
        }
        return teamMember;
    }
}
