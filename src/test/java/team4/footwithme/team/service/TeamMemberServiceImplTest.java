package team4.footwithme.team.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.member.domain.*;
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

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class TeamMemberServiceImplTest extends IntegrationTestSupport {
    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    private Team team;

    @BeforeEach
    void setUp() {
        //팀 생성
        team = Team.create(null, 111L, "테스트 팀이름", "테스트 팀 설명", 0, 0, 0, "서울");
        teamRepository.saveAndFlush(team);
        // 멤버 생성
        memberRepository.save(
                Member.create("member01@gmail.com", "123456", "남남남", "010-1111-1111",
                        LoginProvider.ORIGINAL,"test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE)
        );
       memberRepository.save(
                Member.create("member02@gmail.com", "123456", "남남남", "010-1111-1111",
                        LoginProvider.ORIGINAL,"test", Gender.MALE, MemberRole.USER,TermsAgreed.AGREE)
        );
        memberRepository.save(
                Member.create("member03@gmail.com", "123456", "여여여", "010-1111-1111",
                        LoginProvider.ORIGINAL,"test", Gender.FEMALE, MemberRole.USER,TermsAgreed.AGREE)
        );
    }

    @Test
    @DisplayName("팀원 추가")
    void addTeamMember() {
        //given
        Long teamId = team.getTeamId();
        List<String> emails = new ArrayList<>();
        List<Member> members = memberRepository.findAll();
        for(Member member : members) {
            emails.add(member.getEmail());
        }
        TeamMemberServiceRequest request = new TeamMemberServiceRequest(emails);

        //when
        List<TeamResponse> response = teamMemberService.addTeamMembers(teamId,request);

        //then
        assertThat(response.size()).isEqualTo(3);
        assertThat(response.get(0).name()).isEqualTo("남남남");
        assertThat(response.get(0).role()).isEqualTo(TeamMemberRole.MEMBER);
    }

    @Test
    @DisplayName("팀원 조회")
    public void getTeamMembers() {
        //given
        addTeamMember();
        Long teamId = team.getTeamId();

        //when
        List<TeamResponse> response = teamMemberService.getTeamMembers(teamId);

        //then
        assertThat(response.size()).isEqualTo(3);
        assertThat(response.get(2).name()).isEqualTo("여여여");
        assertThat(response.get(2).role()).isEqualTo(TeamMemberRole.MEMBER);
    }

    @Test
    @DisplayName("팀원 삭제")
    public void deleteTeamMember(){
        //given
        addTeamMember();
        Long teamId = team.getTeamId();
        List<TeamResponse> teamMembers = teamMemberService.getTeamMembers(teamId);
        Long deletedMember = teamMembers.get(1).teamMemberId();

        //when
        Long result = teamMemberService.deleteTeamMembers(deletedMember);
        List<TeamMember> list = teamMemberRepository.findAll();

        //then
        assertThat(result).isEqualTo(deletedMember);
        assertThat(list.get(1).getIsDeleted()).isEqualTo(IsDeleted.TRUE);
    }
}