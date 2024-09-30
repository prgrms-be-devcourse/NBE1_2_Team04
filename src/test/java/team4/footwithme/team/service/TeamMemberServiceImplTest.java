package team4.footwithme.team.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.team.api.request.TeamCreateRequest;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamMemberRole;
import team4.footwithme.team.repository.TeamMemberRepository;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.team.service.request.TeamDefaultServiceRequest;
import team4.footwithme.team.service.request.TeamMemberServiceRequest;
import team4.footwithme.team.service.response.TeamDefaultResponse;
import team4.footwithme.team.service.response.TeamMemberInfoResponse;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class TeamMemberServiceImplTest extends IntegrationTestSupport {
    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Team team;
    private List<Member> members = new ArrayList<>();

    @BeforeEach
    void setUp() {
        //팀 생성
        team = Team.create(null, 111L, "테스트 팀이름", "테스트 팀 설명", 0, 0, 0, "서울");
        teamRepository.saveAndFlush(team);
        // 멤버 생성
        members.add(memberRepository.save(
                Member.create("member01@gmail.com", "123456", "남남남", "010-1111-1111",
                        LoginProvider.ORIGINAL,"test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE)
        ));
        members.add(memberRepository.save(
                Member.create("member02@gmail.com", "123456", "남남남", "010-1111-1111",
                        LoginProvider.ORIGINAL,"test", Gender.MALE, MemberRole.USER,TermsAgreed.AGREE)
        ));
        members.add(memberRepository.save(
                Member.create("member03@gmail.com", "123456", "남남남", "010-1111-1111",
                        LoginProvider.ORIGINAL,"test", Gender.FEMALE, MemberRole.USER,TermsAgreed.AGREE)
        ));
    }

    @Test
    @DisplayName("팀원 추가")
    void addTeamMember() {
        //given
        Long teamId = team.getTeamId();
        List<String> emails = new ArrayList<>();
        for(Member member : members) {
            emails.add(member.getEmail());
        }
        TeamMemberServiceRequest request = new TeamMemberServiceRequest(emails);

        //when
        List<TeamMemberInfoResponse> response = teamMemberService.addTeamMembers(teamId,request);

        //then
        assertThat(response.size()).isEqualTo(3);
        assertThat(response.get(0).teamName()).isEqualTo("테스트 팀이름");
        assertThat(response.get(0).email()).isEqualTo("member01@gmail.com");
        assertThat(response.get(0).role()).isEqualTo(TeamMemberRole.MEMBER);
    }
}