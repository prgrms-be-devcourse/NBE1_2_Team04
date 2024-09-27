package team4.footwithme.vote.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.repository.TeamRepository;
import team4.footwithme.vote.domain.Choice;
import team4.footwithme.vote.domain.Vote;
import team4.footwithme.vote.domain.VoteItem;
import team4.footwithme.vote.domain.VoteItemLocate;
import team4.footwithme.vote.repository.ChoiceRepository;
import team4.footwithme.vote.repository.VoteItemRepository;
import team4.footwithme.vote.repository.VoteRepository;
import team4.footwithme.vote.service.request.VoteStadiumCreateServiceRequest;
import team4.footwithme.vote.service.response.VoteResponse;

import java.time.LocalDateTime;
import java.util.List;

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
    private TeamRepository teamRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    @DisplayName("새로운 구장 투표를 생성한다")
    @Test
    void createStadiumVote() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);
        Stadium givenStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium givenStadium2 = Stadium.create(savedMember, "열정 풋살장", "서울시 강서구 어딘가", "01099999999", "열정 있음", 78.90, 9.876);
        Stadium givenStadium3 = Stadium.create(savedMember, "우주 풋살장", "서울시 동작구 어딘가", "01055555555", "우주에 있음", 65.4321, 12.345);
        Stadium givenStadium4 = Stadium.create(savedMember, "미친 풋살장", "서울시 강북구 어딘가", "01044444444", "강북에 있음", 19.8374, 67.765);

        List<Stadium> savedStadiums = stadiumRepository.saveAll(List.of(givenStadium1, givenStadium2, givenStadium4));
        List<Long> stadiumIds = List.of(savedStadiums.get(0).getStadiumId(), savedStadiums.get(1).getStadiumId(), savedStadiums.get(2).getStadiumId());

        Team team = Team.create(givenStadium1.getStadiumId(), 1L, "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        VoteStadiumCreateServiceRequest request = new VoteStadiumCreateServiceRequest("9월4주차 구장 투표", endAt, stadiumIds);

        //when
        VoteResponse response = voteService.createStadiumVote(request, savedTeam.getTeamId(), "test@gmail.com");
        List<Vote> votes = voteRepository.findAll();
        List<VoteItem> voteItems = voteItemRepository.findAll();
        //then
        assertThat(votes).hasSize(1)
            .extracting("title", "endAt", "memberId", "teamId", "voteId")
            .containsExactlyInAnyOrder(
                tuple("9월4주차 구장 투표", endAt, savedMember.getMemberId(), savedTeam.getTeamId(), votes.get(0).getVoteId())
            );

        assertThat(voteItems).hasSize(3)
            .extracting("vote.voteId", "voteItemId", "stadiumId")
            .containsExactlyInAnyOrder(
                tuple(votes.get(0).getVoteId(), voteItems.get(0).getVoteItemId(), savedStadiums.get(0).getStadiumId()),
                tuple(votes.get(0).getVoteId(), voteItems.get(1).getVoteItemId(), savedStadiums.get(1).getStadiumId()),
                tuple(votes.get(0).getVoteId(), voteItems.get(2).getVoteItemId(), savedStadiums.get(2).getStadiumId())
            );

        assertThat(response).isNotNull()
            .extracting("voteId", "title", "endAt")
            .containsExactlyInAnyOrder(
                votes.get(0).getVoteId(), "9월4주차 구장 투표", endAt
            );

        assertThat(response.choices())
            .hasSize(3)
            .extracting("voteItemId", "content", "voteCount")
            .containsExactlyInAnyOrder(
                tuple(voteItems.get(0).getVoteItemId(), "최강 풋살장", 0L),
                tuple(voteItems.get(1).getVoteItemId(), "열정 풋살장", 0L),
                tuple(voteItems.get(2).getVoteItemId(), "미친 풋살장", 0L)
            );
    }

    @DisplayName("새로운 구장 투표를 생성 할 때 회원 아이디가 존재하지 않으면 예외를 던진다")
    @Test
    void createStadiumVoteWhenMemberIdIsNotExistThrowException() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        List<Long> stadiumIds = List.of(1L, 2L, 3L);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        VoteStadiumCreateServiceRequest request = new VoteStadiumCreateServiceRequest("9월4주차 구장 투표", endAt, stadiumIds);

        //when
        //then
        assertThatThrownBy(()->voteService.createStadiumVote(request, 1L, "error@e.r"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 회원입니다.");
    }

    @DisplayName("새로운 구장 투표를 생성 할 때 팀이 존재하지 않으면 예외를 던진다")
    @Test
    void createStadiumVoteWhenTeamIsNotExistThrowException() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        List<Long> stadiumIds = List.of(1L, 2L, 3L);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        VoteStadiumCreateServiceRequest request = new VoteStadiumCreateServiceRequest("9월4주차 구장 투표", endAt, stadiumIds);

        //when
        //then
        assertThatThrownBy(()->voteService.createStadiumVote(request, -1L, "test@gmail.com"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 팀입니다.");
    }

    @DisplayName("새로운 구장 투표를 생성할 때 존재하지 않는 구장이 포함되어 있으면 예외를 던진다")
    @Test
    void createStadiumVoteWhenStadiumIsNotExistThrowException() {
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);

        List<Long> stadiumIds = List.of(-1L, 1L, 2L);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);
        Stadium givenStadium1 = Stadium.create(savedMember, "최강풋살장", "서울시 강남구 어딘가", "01010101010", "설명", 10.123, 10.123);
        Stadium givenStadium2 = Stadium.create(savedMember, "최강풋살장", "서울시 강남구 어딘가", "01010101010", "설명", 10.123, 10.123);
        Stadium givenStadium3 = Stadium.create(savedMember, "최강풋살장", "서울시 강남구 어딘가", "01010101010", "설명", 10.123, 10.123);

        List<Stadium> savedStadiums = stadiumRepository.saveAll(List.of(givenStadium1, givenStadium2, givenStadium3));

        Team team = Team.create(givenStadium1.getStadiumId(), 1L, "팀이름", "팀 설명", 1, 1, 1, "서울");
        Team savedTeam = teamRepository.save(team);

        VoteStadiumCreateServiceRequest request = new VoteStadiumCreateServiceRequest("9월4주차 구장 투표", endAt, stadiumIds);

        //when
        //then
        assertThatThrownBy(() -> voteService.createStadiumVote(request, savedTeam.getTeamId(), "test@gmail.com"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 구장이 포함되어 있습니다.");
    }

    @DisplayName("투표ID로 구장 투표를 조회한다.")
    @Test
    void getStadiumVoteByVoteId() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium givenStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium givenStadium2 = Stadium.create(savedMember, "열정 풋살장", "서울시 강서구 어딘가", "01099999999", "열정 있음", 78.90, 9.876);
        Stadium givenStadium3 = Stadium.create(savedMember, "우주 풋살장", "서울시 동작구 어딘가", "01055555555", "우주에 있음", 65.4321, 12.345);

        List<Stadium> savedStadiums = stadiumRepository.saveAll(List.of(givenStadium1, givenStadium2, givenStadium3));
        List<Long> stadiumIds = List.of(savedStadiums.get(0).getStadiumId(), savedStadiums.get(1).getStadiumId(), savedStadiums.get(2).getStadiumId());

        Vote vote = Vote.create(1L, 1L, "연말 행사 장소 투표", endAt);

        Vote savedVote = voteRepository.save(vote);

        List<VoteItemLocate> voteItemLocates = stadiumIds.stream()
            .map(stadiumId -> VoteItemLocate.create(savedVote, stadiumId))
            .toList();

        List<VoteItemLocate> savedVoteItems = voteItemRepository.saveAll(voteItemLocates);

        Choice choice1 = Choice.create(10L, savedVoteItems.get(0).getVoteItemId());
        Choice choice2 = Choice.create(10L, savedVoteItems.get(1).getVoteItemId());
        Choice choice3 = Choice.create(20L, savedVoteItems.get(0).getVoteItemId());

        choiceRepository.saveAll(List.of(choice1, choice2, choice3));


        //when
        VoteResponse response = voteService.getStadiumVote(savedVote.getVoteId());

        //then
        Assertions.assertThat(response).isNotNull()
            .extracting("voteId", "title", "endAt")
            .containsExactlyInAnyOrder(
                savedVote.getVoteId(), "연말 행사 장소 투표", endAt
            );

        Assertions.assertThat(response.choices())
            .hasSize(3)
            .extracting("voteItemId", "content", "voteCount")
            .containsExactlyInAnyOrder(
                tuple(savedVoteItems.get(0).getVoteItemId(), "최강 풋살장", 2L),
                tuple(savedVoteItems.get(1).getVoteItemId(), "열정 풋살장", 1L),
                tuple(savedVoteItems.get(2).getVoteItemId(), "우주 풋살장", 0L)
            );
    }

}