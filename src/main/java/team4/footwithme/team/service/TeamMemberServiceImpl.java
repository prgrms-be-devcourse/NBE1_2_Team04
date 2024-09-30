package team4.footwithme.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamMember;
import team4.footwithme.team.domain.TeamMemberRole;
import team4.footwithme.team.repository.TeamMemberRepository;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.team.service.request.TeamMemberServiceRequest;
import team4.footwithme.team.service.response.TeamMemberInfoResponse;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamMemberServiceImpl implements TeamMemberService{


    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    //teamMemberRepository.save(TeamMember.create(team, member01, TeamMemberRole.MEMBER));
    @Override
    public List<TeamMemberInfoResponse> addTeamMembers(Long teamId, TeamMemberServiceRequest request) {
        //팀원 추가할 팀 찾기
        Team team = teamRepository.findByTeamId(teamId);

        //받은 requset에서 email로 각 멤버들의 정보 받아오기
        List<String> emails = request.emails();

        //return할 DTO
        List<TeamMemberInfoResponse> members = new ArrayList<>();
        //member 추가
        for(String email : emails){
            System.out.println(email);
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
            TeamMember teamMember =  teamMemberRepository.save(TeamMember.create(team, member, TeamMemberRole.MEMBER));
            members.add(TeamMemberInfoResponse.of(team,teamMember.getTeamMemberId(),member, teamMember.getRole()));
        }

        return members;
    }
}
