package team4.footwithme.vote.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.CourtRepository;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.vote.domain.*;
import team4.footwithme.vote.repository.ChoiceRepository;
import team4.footwithme.vote.repository.VoteItemRepository;
import team4.footwithme.vote.repository.VoteRepository;
import team4.footwithme.vote.service.request.ChoiceCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteCourtCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteDateCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteUpdateServiceRequest;
import team4.footwithme.vote.service.response.AllVoteResponse;
import team4.footwithme.vote.service.response.VoteResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
class VoteServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private VoteItemRepository voteItemRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("새로운 구장 투표를 생성한다.")
    @Test
    void createCourtVote() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);
        Stadium stadium = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        stadiumRepository.save(stadium);
        Court court1 = Court.create(stadium, "야외 구장 A", "다양한 물품 제공", BigDecimal.TEN);
        Court court2 = Court.create(stadium, "야외 구장 B", "다양한 물품 제공", BigDecimal.TEN);
        Court court3 = Court.create(stadium, "야외 구장 C", "다양한 물품 제공", BigDecimal.TEN);

        List<Court> savedCourts = courtRepository.saveAll(List.of(court1, court2, court3));

        List<Long> courtIds = List.of(savedCourts.get(0).getCourtId(), savedCourts.get(1).getCourtId(), savedCourts.get(2).getCourtId());

        Team team = Team.create(stadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        VoteCourtCreateServiceRequest request = new VoteCourtCreateServiceRequest("9월4주차 구장 투표", endAt, courtIds);

        //when
        VoteResponse response = voteService.createCourtVote(request, savedTeam.getTeamId(), savedMember);
        List<Vote> votes = voteRepository.findAll();
        List<VoteItem> voteItems = voteItemRepository.findAll();
        //then
        assertThat(votes).hasSize(1)
            .extracting("title", "endAt", "memberId", "teamId", "voteId")
            .containsExactlyInAnyOrder(
                tuple("9월4주차 구장 투표", endAt, savedMember.getMemberId(), savedTeam.getTeamId(), votes.get(0).getVoteId())
            );

        assertThat(voteItems).hasSize(3)
            .extracting("vote.voteId", "voteItemId", "courtId")
            .containsExactlyInAnyOrder(
                tuple(votes.get(0).getVoteId(), voteItems.get(0).getVoteItemId(), savedCourts.get(0).getCourtId()),
                tuple(votes.get(0).getVoteId(), voteItems.get(1).getVoteItemId(), savedCourts.get(1).getCourtId()),
                tuple(votes.get(0).getVoteId(), voteItems.get(2).getVoteItemId(), savedCourts.get(2).getCourtId())
            );

        assertThat(response).isNotNull()
            .extracting("voteId", "title", "endAt")
            .containsExactlyInAnyOrder(
                votes.get(0).getVoteId(), "9월4주차 구장 투표", endAt
            );

        assertThat(response.choices())
            .hasSize(3)
            .extracting("voteItemId", "content", "memberIds")
            .containsExactlyInAnyOrder(
                tuple(voteItems.get(0).getVoteItemId(), "야외 구장 A", List.of()),
                tuple(voteItems.get(1).getVoteItemId(), "야외 구장 B", List.of()),
                tuple(voteItems.get(2).getVoteItemId(), "야외 구장 C", List.of())
            );
    }

    @DisplayName("새로운 구장 투표를 생성 할 때 팀이 존재하지 않으면 예외를 던진다")
    @Test
    void createCourtVoteWhenTeamIsNotExistThrowException() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        List<Long> stadiumIds = List.of(1L, 2L, 3L);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        VoteCourtCreateServiceRequest request = new VoteCourtCreateServiceRequest("9월4주차 구장 투표", endAt, stadiumIds);

        //when
        //then
        assertThatThrownBy(() -> voteService.createCourtVote(request, -1L, savedMember))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 팀입니다.");
    }

    @DisplayName("새로운 구장 투표를 생성할 때 존재하지 않는 구장이 포함되어 있으면 예외를 던진다")
    @Test
    void createStadiumVoteWhenCourtIsNotExistThrowException() {
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);

        List<Long> stadiumIds = List.of(-1L, 1L, 2L);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);
        Stadium stadium = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        stadiumRepository.save(stadium);
        Court court1 = Court.create(stadium, "야외 구장 A", "다양한 물품 제공", BigDecimal.TEN);
        Court court2 = Court.create(stadium, "야외 구장 B", "다양한 물품 제공", BigDecimal.TEN);
        Court court3 = Court.create(stadium, "야외 구장 C", "다양한 물품 제공", BigDecimal.TEN);

        List<Court> savedCourts = courtRepository.saveAll(List.of(court1, court2, court3));

        List<Long> courtIds = List.of(savedCourts.get(0).getCourtId(), savedCourts.get(1).getCourtId(), savedCourts.get(2).getCourtId());

        Team team = Team.create(stadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        VoteCourtCreateServiceRequest request = new VoteCourtCreateServiceRequest("9월4주차 구장 투표", endAt, stadiumIds);

        //when
        //then
        assertThatThrownBy(() -> voteService.createCourtVote(request, savedTeam.getTeamId(), savedMember))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 구장이 포함되어 있습니다.");
    }

    @DisplayName("투표ID로 구장 투표를 조회한다.")
    @Test
    void getCourtVoteByVoteId() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium stadium = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        stadiumRepository.save(stadium);
        Court court1 = Court.create(stadium, "야외 구장 A", "다양한 물품 제공", BigDecimal.TEN);
        Court court2 = Court.create(stadium, "야외 구장 B", "다양한 물품 제공", BigDecimal.TEN);
        Court court3 = Court.create(stadium, "야외 구장 C", "다양한 물품 제공", BigDecimal.TEN);

        List<Court> savedCourts = courtRepository.saveAll(List.of(court1, court2, court3));

        List<Long> courtIds = List.of(savedCourts.get(0).getCourtId(), savedCourts.get(1).getCourtId(), savedCourts.get(2).getCourtId());

        Team team = Team.create(stadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        VoteCourtCreateServiceRequest request = new VoteCourtCreateServiceRequest("9월4주차 구장 투표", endAt, courtIds);
        Vote vote = Vote.create(1L, 1L, "연말 행사 장소 투표", endAt);

        Vote savedVote = voteRepository.save(vote);

        List<VoteItemLocate> voteItemLocates = courtIds.stream()
            .map(courtId -> VoteItemLocate.create(savedVote, courtId))
            .toList();

        List<VoteItemLocate> savedVoteItems = voteItemRepository.saveAll(voteItemLocates);

        Choice choice1 = Choice.create(10L, savedVoteItems.get(0).getVoteItemId());
        Choice choice2 = Choice.create(10L, savedVoteItems.get(1).getVoteItemId());
        Choice choice3 = Choice.create(20L, savedVoteItems.get(0).getVoteItemId());

        List<Choice> choices = choiceRepository.saveAll(List.of(choice1, choice2, choice3));


        //when
        VoteResponse response = voteService.getVote(savedVote.getVoteId());

        //then
        assertThat(response).isNotNull()
            .extracting("voteId", "title", "endAt")
            .containsExactlyInAnyOrder(
                savedVote.getVoteId(), "연말 행사 장소 투표", endAt
            );

        assertThat(response.choices())
            .hasSize(3)
            .extracting("voteItemId", "content", "memberIds")
            .containsExactlyInAnyOrder(
                tuple(savedVoteItems.get(0).getVoteItemId(), "야외 구장 A", List.of(choices.get(0).getMemberId(), choices.get(2).getMemberId())),
                tuple(savedVoteItems.get(1).getVoteItemId(), "야외 구장 B", List.of(choices.get(0).getMemberId())),
                tuple(savedVoteItems.get(2).getVoteItemId(), "야외 구장 C", List.of())
            );
    }

    @DisplayName("새로운 일정 투표를 생성한다.")
    @Test
    void createDateVote() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);

        LocalDateTime choice1 = LocalDateTime.now().plusDays(2);
        LocalDateTime choice2 = LocalDateTime.now().plusDays(3);
        LocalDateTime choice3 = LocalDateTime.now().plusDays(4);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium givenStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium savedStadium = stadiumRepository.save(givenStadium1);
        Team team = Team.create(savedStadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        VoteDateCreateServiceRequest request = new VoteDateCreateServiceRequest("연말 경기 투표", endAt, List.of(choice1, choice2, choice3));


        //when
        VoteResponse response = voteService.createDateVote(request, savedTeam.getTeamId(), savedMember);

        List<Vote> votes = voteRepository.findAll();
        List<VoteItem> voteItems = voteItemRepository.findAll();
        //then

        assertThat(votes).hasSize(1)
            .extracting("title", "endAt", "memberId", "teamId", "voteId")
            .containsExactlyInAnyOrder(
                tuple("연말 경기 투표", endAt, savedMember.getMemberId(), savedTeam.getTeamId(), votes.get(0).getVoteId())
            );

        assertThat(voteItems).hasSize(3)
            .extracting("vote.voteId", "voteItemId", "time")
            .containsExactlyInAnyOrder(
                tuple(votes.get(0).getVoteId(), voteItems.get(0).getVoteItemId(), choice1),
                tuple(votes.get(0).getVoteId(), voteItems.get(1).getVoteItemId(), choice2),
                tuple(votes.get(0).getVoteId(), voteItems.get(2).getVoteItemId(), choice3)
            );

        assertThat(response).isNotNull()
            .extracting("voteId", "title", "endAt")
            .containsExactlyInAnyOrder(
                votes.get(0).getVoteId(), "연말 경기 투표", endAt
            );

        assertThat(response.choices())
            .hasSize(3)
            .extracting("voteItemId", "content", "memberIds")
            .containsExactlyInAnyOrder(
                tuple(voteItems.get(0).getVoteItemId(), choice1.toString(), List.of()),
                tuple(voteItems.get(1).getVoteItemId(), choice2.toString(), List.of()),
                tuple(voteItems.get(2).getVoteItemId(), choice3.toString(), List.of())
            );
    }

    @DisplayName("일정 투표를 투표 ID로 조회한다.")
    @Test
    void getDateVoteByVoteId() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);

        LocalDateTime choice1 = LocalDateTime.now().plusDays(2);
        LocalDateTime choice2 = LocalDateTime.now().plusDays(3);
        LocalDateTime choice3 = LocalDateTime.now().plusDays(4);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium givenStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium savedStadium = stadiumRepository.save(givenStadium1);
        Team team = Team.create(savedStadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        Vote vote = Vote.create(1L, 1L, "연말 경기 투표", endAt);
        Vote savedVote = voteRepository.save(vote);

        VoteItem voteItem1 = VoteItemDate.create(savedVote, choice1);
        VoteItem voteItem2 = VoteItemDate.create(savedVote, choice2);
        VoteItem voteItem3 = VoteItemDate.create(savedVote, choice3);

        List<VoteItem> savedVoteItems = voteItemRepository.saveAll(List.of(voteItem1, voteItem2, voteItem3));

        //when
        VoteResponse response = voteService.getVote(savedVote.getVoteId());

        //then
        assertThat(response).isNotNull()
            .extracting("voteId", "title", "endAt")
            .containsExactlyInAnyOrder(
                savedVote.getVoteId(), "연말 경기 투표", endAt
            );

        assertThat(response.choices())
            .hasSize(3)
            .extracting("voteItemId", "content", "memberIds")
            .containsExactlyInAnyOrder(
                tuple(savedVoteItems.get(0).getVoteItemId(), choice1.toString(), List.of()),
                tuple(savedVoteItems.get(1).getVoteItemId(), choice2.toString(), List.of()),
                tuple(savedVoteItems.get(2).getVoteItemId(), choice3.toString(), List.of())
            );
    }

    @Disabled("스케쥴러 문제가 있음 추후 수정 필요")
    @DisplayName("투표를 투표 ID로 삭제한다.")
    @Test
    void deleteVoteByVoteId() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);

        LocalDateTime choice1 = LocalDateTime.now().plusDays(2);
        LocalDateTime choice2 = LocalDateTime.now().plusDays(3);
        LocalDateTime choice3 = LocalDateTime.now().plusDays(4);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium stadium = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        stadiumRepository.save(stadium);
        Court court1 = Court.create(stadium, "야외 구장 A", "다양한 물품 제공", BigDecimal.TEN);
        Court court2 = Court.create(stadium, "야외 구장 B", "다양한 물품 제공", BigDecimal.TEN);
        Court court3 = Court.create(stadium, "야외 구장 C", "다양한 물품 제공", BigDecimal.TEN);

        List<Court> savedCourts = courtRepository.saveAll(List.of(court1, court2, court3));

        List<Long> courtIds = List.of(savedCourts.get(0).getCourtId(), savedCourts.get(1).getCourtId(), savedCourts.get(2).getCourtId());

        Team team = Team.create(stadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        Vote vote = Vote.create(savedMember.getMemberId(), 1L, "연말 경기 투표", endAt);
        Vote savedVote = voteRepository.save(vote);

        VoteItem voteItem1 = VoteItemDate.create(savedVote, choice1);
        VoteItem voteItem2 = VoteItemDate.create(savedVote, choice2);
        VoteItem voteItem3 = VoteItemDate.create(savedVote, choice3);

        List<VoteItem> savedVoteItems = voteItemRepository.saveAll(List.of(voteItem1, voteItem2, voteItem3));
        //when
        Long deletedId = voteService.deleteVote(savedVote.getVoteId(), savedMember);

        // @SQLDelete를 사용하면 수동으로 flush 해야함
        entityManager.flush();

        //then
        Optional<Vote> deletedVote = voteRepository.findById(deletedId);

        assertThat(deletedVote.isPresent()).isTrue();
        assertThat(deletedVote.get()).extracting(
            "voteId", "title", "endAt", "memberId", "teamId", "isDeleted"
        ).containsExactly(
            deletedId, "연말 경기 투표", endAt, deletedVote.get().getMemberId(), deletedVote.get().getTeamId(), IsDeleted.TRUE
        );
    }

    @DisplayName("투표의 상세 항목을 투표한다.")
    @Test
    void createChoice() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);

        LocalDateTime choice1 = LocalDateTime.now().plusDays(2);
        LocalDateTime choice2 = LocalDateTime.now().plusDays(3);
        LocalDateTime choice3 = LocalDateTime.now().plusDays(4);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium givenStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium savedStadium = stadiumRepository.save(givenStadium1);
        Team team = Team.create(savedStadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        Vote vote = Vote.create(1L, 1L, "연말 경기 투표", endAt);
        Vote savedVote = voteRepository.save(vote);

        VoteItem voteItem1 = VoteItemDate.create(savedVote, choice1);
        VoteItem voteItem2 = VoteItemDate.create(savedVote, choice2);
        VoteItem voteItem3 = VoteItemDate.create(savedVote, choice3);

        List<VoteItem> savedVoteItems = voteItemRepository.saveAll(List.of(voteItem1, voteItem2, voteItem3));

        ChoiceCreateServiceRequest request = new ChoiceCreateServiceRequest(List.of(savedVoteItems.get(0).getVoteItemId(), savedVoteItems.get(1).getVoteItemId()));

        //when
        VoteResponse response = voteService.createChoice(request, savedVote.getVoteId(), savedMember);

        List<Choice> choices = choiceRepository.findAll();

        //then
        assertThat(response)
            .extracting("voteId", "title", "endAt")
            .containsExactlyInAnyOrder(
                savedVote.getVoteId(), "연말 경기 투표", endAt
            );

        assertThat(response.choices())
            .hasSize(3)
            .extracting("voteItemId", "content", "memberIds")
            .containsExactlyInAnyOrder(
                tuple(savedVoteItems.get(0).getVoteItemId(), choice1.toString(), List.of(choices.get(0).getMemberId())),
                tuple(savedVoteItems.get(1).getVoteItemId(), choice2.toString(), List.of(choices.get(0).getMemberId())),
                tuple(savedVoteItems.get(2).getVoteItemId(), choice3.toString(), List.of())
            );

        assertThat(choices)
            .extracting("memberId", "voteItemId")
            .containsExactlyInAnyOrder(
                tuple(savedMember.getMemberId(), savedVoteItems.get(0).getVoteItemId()),
                tuple(savedMember.getMemberId(), savedVoteItems.get(1).getVoteItemId())
            );
    }

    @DisplayName("자신이 한 투표를 취소한다.")
    @Test
    void deleteChoice() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);

        LocalDateTime choice1 = LocalDateTime.now().plusDays(2);
        LocalDateTime choice2 = LocalDateTime.now().plusDays(3);
        LocalDateTime choice3 = LocalDateTime.now().plusDays(4);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium givenStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium savedStadium = stadiumRepository.save(givenStadium1);
        Team team = Team.create(savedStadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        Vote vote = Vote.create(1L, 1L, "연말 경기 투표", endAt);
        Vote savedVote = voteRepository.save(vote);

        VoteItem voteItem1 = VoteItemDate.create(savedVote, choice1);
        VoteItem voteItem2 = VoteItemDate.create(savedVote, choice2);
        VoteItem voteItem3 = VoteItemDate.create(savedVote, choice3);

        List<VoteItem> savedVoteItems = voteItemRepository.saveAll(List.of(voteItem1, voteItem2, voteItem3));


        Choice memberChoice1 = Choice.create(savedMember.getMemberId(), savedVoteItems.get(0).getVoteItemId());
        Choice memberChoice2 = Choice.create(savedMember.getMemberId(), savedVoteItems.get(1).getVoteItemId());
        choiceRepository.saveAll(List.of(memberChoice1, memberChoice2));
        //when
        VoteResponse response = voteService.deleteChoice(savedVote.getVoteId(), savedMember);

        //then
        assertThat(response)
            .extracting("voteId", "title", "endAt")
            .containsExactlyInAnyOrder(
                savedVote.getVoteId(), "연말 경기 투표", endAt
            );

        assertThat(response.choices())
            .hasSize(3)
            .extracting("voteItemId", "content", "memberIds")
            .containsExactlyInAnyOrder(
                tuple(savedVoteItems.get(0).getVoteItemId(), choice1.toString(), List.of()),
                tuple(savedVoteItems.get(1).getVoteItemId(), choice2.toString(), List.of()),
                tuple(savedVoteItems.get(2).getVoteItemId(), choice3.toString(), List.of())
            );
    }

    @Disabled("스케쥴러의 문제가 존재함 추후 수정 필요")
    @DisplayName("투표의 제목을 변경한다.")
    @Test
    void updateVote() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);

        LocalDateTime choice1 = LocalDateTime.now().plusDays(2);
        LocalDateTime choice2 = LocalDateTime.now().plusDays(3);
        LocalDateTime choice3 = LocalDateTime.now().plusDays(4);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium givenStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium savedStadium = stadiumRepository.save(givenStadium1);
        Team team = Team.create(savedStadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        Vote vote = Vote.create(savedMember.getMemberId(), 1L, "연말 경기 투표", endAt);
        Vote savedVote = voteRepository.save(vote);

        VoteItem voteItem1 = VoteItemDate.create(savedVote, choice1);
        VoteItem voteItem2 = VoteItemDate.create(savedVote, choice2);
        VoteItem voteItem3 = VoteItemDate.create(savedVote, choice3);

        List<VoteItem> savedVoteItems = voteItemRepository.saveAll(List.of(voteItem1, voteItem2, voteItem3));

        VoteUpdateServiceRequest request = new VoteUpdateServiceRequest("연말 경기 투표 수정", endAt);
        //when
        VoteResponse response = voteService.updateVote(request, savedVote.getVoteId(), savedMember);

        //then
        assertThat(response)
            .extracting("voteId", "title", "endAt")
            .containsExactlyInAnyOrder(
                savedVote.getVoteId(), "연말 경기 투표 수정", endAt
            );
    }

    @Disabled("신뢰성이 없는 테스트임 추후 수정이 따로 필요함 Proudct 환경과 간극이 존재함")
    @DisplayName("투표 마감시간이되면 투표를 종료한다.")
    @Test
    void addVoteTaskToTaskSchedule() throws InterruptedException {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusSeconds(1);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);
        Stadium stadium = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        stadiumRepository.save(stadium);
        Court court1 = Court.create(stadium, "야외 구장 A", "다양한 물품 제공", BigDecimal.TEN);
        Court court2 = Court.create(stadium, "야외 구장 B", "다양한 물품 제공", BigDecimal.TEN);
        Court court3 = Court.create(stadium, "야외 구장 C", "다양한 물품 제공", BigDecimal.TEN);

        List<Court> savedCourts = courtRepository.saveAll(List.of(court1, court2, court3));

        List<Long> courtIds = List.of(savedCourts.get(0).getCourtId(), savedCourts.get(1).getCourtId(), savedCourts.get(2).getCourtId());

        Team team = Team.create(stadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        VoteCourtCreateServiceRequest request = new VoteCourtCreateServiceRequest("9월4주차 구장 투표", endAt, courtIds);

        //when

        VoteResponse response = voteService.createCourtVote(request, savedTeam.getTeamId(), savedMember);
        //then
        assertThat(response).isNotNull()
            .extracting("voteId", "title", "endAt")
            .containsExactlyInAnyOrder(
                response.voteId(), "9월4주차 구장 투표", endAt
            );

        System.out.println("실행 할 시간 확인" + LocalDateTime.now());
        Optional<Vote> updateVote = voteRepository.findById(response.voteId());
        Thread.sleep(2000);
        assertThat(updateVote.get()).extracting(
                "voteId", "title", "endAt", "voteStatus")
            .containsExactly(
                response.voteId(), "9월4주차 구장 투표", endAt, VoteStatus.CLOSED
            );
    }

    @Disabled("돌려도 통과는 하는데 스케쥴러가 취소되는지는 테스트가 안되고 있어서 테스트의 신뢰성이 부족함")
    @DisplayName("투표 종료요청이 생기면 투표를 종료하고, 스케쥴러에서 제외시킨다.")
    @Test
    void closeVote() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);

        LocalDateTime choice1 = LocalDateTime.now().plusDays(2);
        LocalDateTime choice2 = LocalDateTime.now().plusDays(3);
        LocalDateTime choice3 = LocalDateTime.now().plusDays(4);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium givenStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium savedStadium = stadiumRepository.save(givenStadium1);
        Team team = Team.create(savedStadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        Vote vote = Vote.create(1L, 1L, "연말 경기 투표", endAt);
        Vote savedVote = voteRepository.save(vote);

        VoteItem voteItem1 = VoteItemDate.create(savedVote, choice1);
        VoteItem voteItem2 = VoteItemDate.create(savedVote, choice2);
        VoteItem voteItem3 = VoteItemDate.create(savedVote, choice3);

        List<VoteItem> savedVoteItems = voteItemRepository.saveAll(List.of(voteItem1, voteItem2, voteItem3));

        //when
        VoteResponse response = voteService.closeVote(savedVote.getVoteId(), savedMember);
        //then
        assertThat(response)
            .extracting("voteId", "title", "endAt", "voteStatus")
            .containsExactlyInAnyOrder(
                response.voteId(), "연말 경기 투표", endAt, VoteStatus.CLOSED.getText()
            );
    }

    @DisplayName("팀의 전체 투표 목록을 조회한다.")
    @Test
    void findAllByTeamId() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);

        LocalDateTime choice1 = LocalDateTime.now().plusDays(2);
        LocalDateTime choice2 = LocalDateTime.now().plusDays(3);
        LocalDateTime choice3 = LocalDateTime.now().plusDays(4);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium givenStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium savedStadium = stadiumRepository.save(givenStadium1);
        Team team = Team.create(savedStadium.getStadiumId(), "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        Vote vote1 = Vote.create(1L, savedTeam.getTeamId(), "연말 경기 투표", endAt);
        Vote vote2 = Vote.create(1L, savedTeam.getTeamId(), "축구 경기 투표", endAt);
        List<Vote> savedVotes = voteRepository.saveAll(List.of(vote1, vote2));

        VoteItem voteItem1 = VoteItemDate.create(savedVotes.get(0), choice1);
        VoteItem voteItem2 = VoteItemDate.create(savedVotes.get(0), choice2);
        VoteItem voteItem3 = VoteItemDate.create(savedVotes.get(0), choice3);

        List<VoteItem> savedVoteItems = voteItemRepository.saveAll(List.of(voteItem1, voteItem2, voteItem3));
        //when
        List<AllVoteResponse> response = voteService.getAllVotes(savedTeam.getTeamId());
        //then
        assertThat(response).hasSize(2)
            .extracting("voteId", "title", "endAt", "status")
            .containsExactlyInAnyOrder(
                tuple(savedVotes.get(0).getVoteId(), "연말 경기 투표", endAt, VoteStatus.OPENED.getText()),
                tuple(savedVotes.get(1).getVoteId(), "축구 경기 투표", endAt, VoteStatus.OPENED.getText())
            );
    }

}