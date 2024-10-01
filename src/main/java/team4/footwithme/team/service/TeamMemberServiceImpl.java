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
        Team team = teamRepository.findByTeamId(teamId);

        //팀 정보가 없을 때 예외처리
        if(team == null) {
            throw new IllegalArgumentException("해당 팀이 존재하지 않습니다.");
        }

        //받은 requset에서 email로 각 멤버들의 정보 받아오기
        List<String> emails = request.emails();

        //return할 DTO
        List<TeamResponse> members = new ArrayList<>();
        //member 추가
        for(String email : emails){
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

            TeamMember teamMember =  teamMemberRepository.save(TeamMember.create(team, member, TeamMemberRole.MEMBER));

            members.add(TeamResponse.of(teamMember));
        }
        return members;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponse> getTeamMembers(Long teamId) {
        //팀 찾기
        Team team = teamRepository.findByTeamId(teamId);

        //팀 정보가 없을 때 예외처리
        if(team == null) {
            throw new IllegalArgumentException("해당 팀이 존재하지 않습니다.");
        }
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);
        List<TeamResponse> members = new ArrayList<>();
        for(TeamMember teamMember : teamMembers){
            members.add(TeamResponse.of(teamMember));
        }
        return members;
    }

    @Override
    @Transactional
    public Long deleteTeamMembers(Long teamMemberId) {
        //팀 멤버 찾기
        TeamMember teamMember = teamMemberRepository.findByTeamMemberId(teamMemberId);

        if(teamMember == null) {
            throw new IllegalArgumentException("존재하지 않는 팀원입니다.");
        }

        teamMemberRepository.delete(teamMember);
        return teamMemberId;
    }
}
