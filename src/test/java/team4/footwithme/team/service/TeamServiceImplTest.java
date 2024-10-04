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
import team4.footwithme.team.api.request.TeamCreateRequest;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamMember;
import team4.footwithme.team.domain.TeamMemberRole;
import team4.footwithme.team.domain.TeamRate;
import team4.footwithme.team.repository.TeamMemberRepository;
import team4.footwithme.team.repository.TeamRateRepository;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.team.service.request.TeamDefaultServiceRequest;
import team4.footwithme.team.service.response.TeamDefaultResponse;
import team4.footwithme.team.service.response.TeamInfoResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


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

    @BeforeEach
    void setUp() {
        //팀장용 멤버 생성
        memberRepository.save(
                Member.create("teamLeader@gmail.com", "123456", "팀장", "010-1111-1111",
                        LoginProvider.ORIGINAL,"test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE)
        );
        // 멤버 생성
        memberRepository.save(
                Member.create("member01@gmail.com", "123456", "남팀원01", "010-1111-1111",
                        LoginProvider.ORIGINAL,"test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE)
        );
        memberRepository.save(
                Member.create("member02@gmail.com", "123456", "남팀원02", "010-1111-1111",
                        LoginProvider.ORIGINAL,"test", Gender.MALE, MemberRole.USER,TermsAgreed.AGREE)
        );
        memberRepository.save(
                Member.create("member03@gmail.com", "123456", "여팀원01", "010-1111-1111",
                        LoginProvider.ORIGINAL,"test", Gender.FEMALE, MemberRole.USER,TermsAgreed.AGREE)
        );
    }
    @Test
    @DisplayName("팀 생성")
    void createTeam() {
        //given
        TeamCreateRequest request = new TeamCreateRequest("팀명", "팀 설명", "선호지역");
        Member leaderMember = memberRepository.findByEmail("teamLeader@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        TeamDefaultServiceRequest dto = request.toServiceRequest();

        //when
        teamService.createTeam(dto,leaderMember);
        long count = teamRepository.count();

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("팀 정보 조회")
    void getTeamInfo(){
        // Given
        Member leader = memberRepository.findByEmail("teamLeader@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        Team team = teamRepository.save(Team.create(null,1L, "팀명","팀 설명", 0, 0, 0,"선호지역"));
        teamMemberRepository.save(TeamMember.createCreator(team, leader));

        // 등록할 멤버
        Member member01 = memberRepository.findByEmail("member01@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        Member member02 = memberRepository.findByEmail("member02@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        Member member03 = memberRepository.findByEmail("member03@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        // 팀 멤버 등록
        teamMemberRepository.save(TeamMember.createMember(team, member01));
        teamMemberRepository.save(TeamMember.createMember(team, member02));
        teamMemberRepository.save(TeamMember.createMember(team, member03));

        // 팀 평가 저장
        teamRateRepository.save(new TeamRate(team, 5.0, "좋았다"));
        teamRateRepository.save(new TeamRate(team, 2.0, "별로였다"));

        // When
        TeamInfoResponse response = teamService.getTeamInfo(team.getTeamId());

        // Then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("팀명");
        assertThat(response.winCount()).isEqualTo(0);
        assertThat(response.evaluation().size()).isEqualTo(2);
        assertThat(response.maleCount()).isEqualTo(3);
        assertThat(response.femaleCount()).isEqualTo(1);

    }

    @Test
    @DisplayName("팀 정보 수정_팀장")
    void updateTeamInfo(){
        //given
        //팀 정보 저장
        Member leader = memberRepository.findByEmail("teamLeader@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        Team team = teamRepository.save(Team.create(null,1L, "팀명","팀 설명", 0, 0, 0,"선호지역"));
        teamMemberRepository.save(TeamMember.createCreator(team, leader));

        //when
        //팀 정보 수정
        TeamDefaultServiceRequest updateDTO = new TeamDefaultServiceRequest(null, "우리애 월드클래스 아닙니다.", "서울 전역");
        TeamDefaultResponse result = teamService.updateTeamInfo(team.getTeamId(), updateDTO, leader);

        //then
        assertThat(result).isNotNull();
        //팀 이름 안바꿈
        assertThat(result.name()).isEqualTo("팀명");
        //팀 설명 바꿈
        assertThat(result.description()).isEqualTo(updateDTO.description());
        //선호 지역 바꿈
        assertThat(result.location()).isEqualTo(updateDTO.location());
    }

    @Test
    @DisplayName("팀 정보 수정_멤버")
    void updateTeamInfo_exception(){
        //given
        //팀 정보 저장
        Member leaderMember = memberRepository.findByEmail("teamLeader@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        Member member01 = memberRepository.findByEmail("member01@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        Team team = teamRepository.save(Team.create(null,1L, "팀명","팀 설명", 0, 0, 0,"선호지역"));
        teamMemberRepository.save(TeamMember.createCreator(team, leaderMember));
        teamMemberRepository.save(TeamMember.createMember(team, member01));

        //when
        //팀 정보 수정
        TeamDefaultServiceRequest updateDTO = new TeamDefaultServiceRequest(null, "우리애 월드클래스 아닙니다.", "서울 전역");

        //then -- 팀원이 정보 수정 시 예외
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->{
            teamService.updateTeamInfo(team.getTeamId(), updateDTO, member01);
        });
    }

    @Test
    @DisplayName("팀 삭제")
    void deleteTeam(){
        //given
        //팀 정보 저장
        Member leader = memberRepository.findByEmail("teamLeader@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        Team team = teamRepository.save(Team.create(null,1L, "팀명","팀 설명", 0, 0, 0,"선호지역"));
        teamMemberRepository.save(TeamMember.createCreator(team, leader));

        //when
        Long teamId = teamService.deleteTeam(team.getTeamId(), leader);
        Team result = teamRepository.findById(teamId)
                .orElse(null);

        //then --> 삭제되었으므로 select 시 제외됨
        assertThat(result).isNull();
    }
}