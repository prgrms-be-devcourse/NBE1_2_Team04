package team4.footwithme.team.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.team.api.request.TeamAddResquest;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamMember;
import team4.footwithme.team.domain.TeamMemberRole;
import team4.footwithme.team.domain.TeamRate;
import team4.footwithme.team.repository.TeamMemberRepository;
import team4.footwithme.team.repository.TeamRateRepository;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.team.service.response.TeamCreatedResponse;
import team4.footwithme.team.service.response.TeamInfoResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TeamServiceImplTest {
    @Autowired
    private TeamServiceImpl teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRateRepository teamRateRepository;
    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Test
    @Transactional
    @DisplayName("팀 생성")
    void createTeam() {
        //given
        TeamAddResquest dto = new TeamAddResquest("팀명", "팀 설명", "선호지역");
        teamService.createTeam(dto);

        //when
        long count = teamRepository.count();

        //then
        assertThat(count).isEqualTo(1);
    }

    // 멤버를 생성하는 팩토리 메소드
    private Member createMember(String email, String password, String name, String phoneNumber, LoginType loginType, Gender gender) {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .phoneNumber(phoneNumber)
                .loginType(loginType)
                .gender(gender)
                .memberRole(MemberRole.USER)
                .termsAgreed(TermsAgreed.AGREE)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("팀 정보 조회")
    void getTeamInfo(){
        // Given
        TeamAddResquest dto = new TeamAddResquest("팀명", "팀 설명", "선호지역");
        TeamCreatedResponse teamCreatedResponse = teamService.createTeam(dto);
        Long teamId = teamCreatedResponse.teamId();
        Team team = teamRepository.findByTeamId(teamId);

        // 멤버 생성 및 저장
        Member member01 = memberRepository.save(createMember("member01@gmail.com", "123456", "남남남", "010-1111-1111",
                LoginType.builder().loginProvider(LoginProvider.KAKAO).snsId("member01@kakao.com").build(), Gender.MALE));

        Member member02 = memberRepository.save(createMember("member02@gmail.com", "123", "남남2", "010-1231-1111",
                LoginType.builder().loginProvider(LoginProvider.GOOGLE).snsId("member02@google.com").build(), Gender.MALE));

        Member member03 = memberRepository.save(createMember("member03@gmail.com", "6548", "여여여", "010-1111-9871",
                LoginType.builder().loginProvider(LoginProvider.KAKAO).snsId("member03@kakao.com").build(), Gender.FEMALE));

        // 팀 멤버 등록
        teamMemberRepository.save(TeamMember.create(team, member01, TeamMemberRole.MEMBER));
        teamMemberRepository.save(TeamMember.create(team, member02, TeamMemberRole.MEMBER));
        teamMemberRepository.save(TeamMember.create(team, member03, TeamMemberRole.MEMBER));

        // 팀 평가 저장
        teamRateRepository.save(new TeamRate(team, 5.0, "좋았다"));
        teamRateRepository.save(new TeamRate(team, 2.0, "별로였다"));

        // When
        TeamInfoResponse response = teamService.getTeamInfo(teamId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("팀명");
        assertThat(response.winCount()).isEqualTo(0);
        assertThat(response.evaluation().size()).isEqualTo(2);
        assertThat(response.maleCount()).isEqualTo(2);

    }
}