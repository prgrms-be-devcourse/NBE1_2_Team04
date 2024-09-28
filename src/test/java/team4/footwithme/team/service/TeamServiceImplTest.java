package team4.footwithme.team.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.team.api.request.TeamCreateRequest;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamMember;
import team4.footwithme.team.domain.TeamMemberRole;
import team4.footwithme.team.domain.TeamRate;
import team4.footwithme.team.repository.TeamMemberRepository;
import team4.footwithme.team.repository.TeamRateRepository;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.team.service.request.TeamCreateServiceRequest;
import team4.footwithme.team.service.request.TeamUpdateServiceRequest;
import team4.footwithme.team.service.response.TeamCreatedResponse;
import team4.footwithme.team.service.response.TeamInfoResponse;
import team4.footwithme.team.service.response.TeamUpdateResponse;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
class TeamServiceImplTest extends IntegrationTestSupport {
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
    @DisplayName("팀 생성")
    void createTeam() {
        //given
        TeamCreateRequest request = new TeamCreateRequest("팀명", "팀 설명", "선호지역");
        TeamCreateServiceRequest dto = request.toServiceRequest();
        teamService.createTeam(dto);

        //when
        long count = teamRepository.count();

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("팀 정보 조회")
    void getTeamInfo(){
        // Given
        TeamCreateRequest request = new TeamCreateRequest("팀명", "팀 설명", "선호지역");
        TeamCreateServiceRequest dto = request.toServiceRequest();
        TeamCreatedResponse teamCreatedResponse = teamService.createTeam(dto);
        Long teamId = teamCreatedResponse.teamId();
        Team team = teamRepository.findByTeamId(teamId);

        // 멤버 생성 및 저장
        Member member01 = memberRepository.save(
                Member.create("member01@gmail.com", "123456", "남남남", "010-1111-1111",
                LoginProvider.ORIGINAL,"test", Gender.MALE, MemberRole.USER,TermsAgreed.AGREE)
        );

        Member member02 = memberRepository.save(
                Member.create("member02@gmail.com", "123456", "남남남", "010-1111-1111",
                LoginProvider.ORIGINAL,"test", Gender.MALE, MemberRole.USER,TermsAgreed.AGREE)
        );

        Member member03 = memberRepository.save(
                Member.create("member03@gmail.com", "123456", "남남남", "010-1111-1111",
                LoginProvider.ORIGINAL,"test", Gender.FEMALE, MemberRole.USER,TermsAgreed.AGREE)
        );

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

    //궁금점.. 수정 쿼리가 아니고 insert쿼리가 날라가도 되는건가?
    @Test
    @DisplayName("팀 정보 수정")
    void updateTeamInfo(){
        //given
        //팀 정보 저장
        TeamCreateRequest request = new TeamCreateRequest("팀명", "팀 설명", "선호지역");
        TeamCreateServiceRequest dto = request.toServiceRequest();
        TeamCreatedResponse beforeEntity = teamService.createTeam(dto);

        //when
        //팀 정보 수정
        Long teamId = beforeEntity.teamId();
        TeamUpdateServiceRequest updateDTO = new TeamUpdateServiceRequest(null, "우리애 월드클래스 아닙니다.", "서울 전역");
        TeamUpdateResponse result = teamService.updateTeamInfo(teamId, updateDTO);

        //then
        assertThat(result).isNotNull();
        //팀 이름 안바꿈
        assertThat(result.name()).isEqualTo(beforeEntity.name());
        //팀 설명 바꿈
        assertThat(result.description()).isEqualTo(updateDTO.description());
        //선호 지역 바꿈
        assertThat(result.location()).isEqualTo(updateDTO.location());
    }
}