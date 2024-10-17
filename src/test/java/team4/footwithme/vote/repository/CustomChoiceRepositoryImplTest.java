package team4.footwithme.vote.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.vote.domain.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
class CustomChoiceRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private VoteItemRepository voteItemRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StadiumRepository stadiumRepository;
    @Autowired
    private ChoiceRepository choiceRepository;


    @DisplayName("투표항목의 아이디로 항목이 선택된 횟수를 조회한다.")
    @Test
    void countByVoteItemId() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium givenStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium givenStadium2 = Stadium.create(savedMember, "열정 풋살장", "서울시 강서구 어딘가", "01099999999", "열정 있음", 78.90, 9.876);
        Stadium givenStadium3 = Stadium.create(savedMember, "우주 풋살장", "서울시 동작구 어딘가", "01055555555", "우주에 있음", 65.4321, 12.345);

        List<Stadium> savedStadiums = stadiumRepository.saveAll(List.of(givenStadium1, givenStadium2, givenStadium3));
        List<Long> stadiumIds = List.of(savedStadiums.get(0).stadiumId, savedStadiums.get(1).stadiumId, savedStadiums.get(2).stadiumId);

        Vote vote = Vote.create(1L, 1L, "연말 행사 장소 투표", endAt);

        Vote savedVote = voteRepository.save(vote);

        List<VoteItemLocate> voteItemLocates = stadiumIds.stream()
            .map(stadiumId -> VoteItemLocate.create(savedVote, stadiumId))
            .toList();

        List<VoteItemLocate> savedVoteItems = voteItemRepository.saveAll(voteItemLocates);

        Choice choice1 = Choice.create(savedMember.memberId, savedVoteItems.get(0).voteItemId);
        Choice choice2 = Choice.create(savedMember.memberId, savedVoteItems.get(1).voteItemId);

        List<Choice> choices = choiceRepository.saveAll(List.of(choice1, choice2));
        //when
        Long count = choiceRepository.countByVoteItemId(savedVoteItems.get(0).voteItemId);

        //then
        assertThat(count).isEqualTo(1L);
    }

    @DisplayName("회원의 아이디와 투표의 아이디로 선택한 항목을 조회한다.")
    @Test
    void findByMemberIdAndVoteId() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium givenStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium givenStadium2 = Stadium.create(savedMember, "열정 풋살장", "서울시 강서구 어딘가", "01099999999", "열정 있음", 78.90, 9.876);
        Stadium givenStadium3 = Stadium.create(savedMember, "우주 풋살장", "서울시 동작구 어딘가", "01055555555", "우주에 있음", 65.4321, 12.345);

        List<Stadium> savedStadiums = stadiumRepository.saveAll(List.of(givenStadium1, givenStadium2, givenStadium3));
        List<Long> stadiumIds = List.of(savedStadiums.get(0).stadiumId, savedStadiums.get(1).stadiumId, savedStadiums.get(2).stadiumId);

        Vote vote = Vote.create(1L, 1L, "연말 행사 장소 투표", endAt);

        Vote savedVote = voteRepository.save(vote);

        List<VoteItemLocate> voteItemLocates = stadiumIds.stream()
            .map(stadiumId -> VoteItemLocate.create(savedVote, stadiumId))
            .toList();

        List<VoteItemLocate> savedVoteItems = voteItemRepository.saveAll(voteItemLocates);

        Choice choice1 = Choice.create(savedMember.memberId, savedVoteItems.get(0).voteItemId);
        Choice choice2 = Choice.create(savedMember.memberId, savedVoteItems.get(1).voteItemId);

        List<Choice> choices = choiceRepository.saveAll(List.of(choice1, choice2));

        //when
        List<Choice> findChoices = choiceRepository.findByMemberIdAndVoteId(savedMember.memberId, savedVote.voteId);

        //then
        assertThat(findChoices).hasSize(2)
            .extracting("memberId", "voteItemId")
            .containsExactlyInAnyOrder(
                tuple(savedMember.memberId, savedVoteItems.get(0).voteItemId),
                tuple(savedMember.memberId, savedVoteItems.get(1).voteItemId)
            );
    }

    @DisplayName("투표 항목 중 1등을 뽑는다.")
    @Test
    void maxChoiceCountByVoteId() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);

        LocalDateTime givenVoteItem1 = LocalDateTime.now().plusDays(2);
        LocalDateTime givenVoteItem2 = LocalDateTime.now().plusDays(3);
        LocalDateTime givenVoteItem3 = LocalDateTime.now().plusDays(4);

        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);

        Vote vote = Vote.create(1L, 1L, "연말 경기 투표", endAt);
        Vote savedVote = voteRepository.save(vote);

        VoteItem voteItem1 = VoteItemDate.create(savedVote, givenVoteItem1);
        VoteItem voteItem2 = VoteItemDate.create(savedVote, givenVoteItem2);
        VoteItem voteItem3 = VoteItemDate.create(savedVote, givenVoteItem3);

        List<VoteItem> savedVoteItems = voteItemRepository.saveAll(List.of(voteItem1, voteItem2, voteItem3));

        Choice choice1 = Choice.create(1L, savedVoteItems.get(0).voteItemId);
        Choice choice2 = Choice.create(1L, savedVoteItems.get(1).voteItemId);
        Choice choice3 = Choice.create(2L, savedVoteItems.get(1).voteItemId);
        Choice choice4 = Choice.create(3L, savedVoteItems.get(0).voteItemId);
        Choice choice5 = Choice.create(3L, savedVoteItems.get(1).voteItemId);
        Choice choice6 = Choice.create(3L, savedVoteItems.get(2).voteItemId);

        choiceRepository.saveAll(List.of(choice1, choice2, choice3, choice4, choice5, choice6));

        //when
        Long voteId = choiceRepository.maxChoiceCountByVoteId(savedVote.voteId);
        //then
        assertThat(voteId).isEqualTo(savedVoteItems.get(1).voteItemId);
    }

}