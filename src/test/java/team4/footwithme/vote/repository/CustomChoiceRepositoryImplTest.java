package team4.footwithme.vote.repository;

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
import team4.footwithme.vote.domain.Choice;
import team4.footwithme.vote.domain.Vote;
import team4.footwithme.vote.domain.VoteItemLocate;

import java.time.LocalDateTime;
import java.util.List;

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
        List<Long> stadiumIds = List.of(savedStadiums.get(0).getStadiumId(), savedStadiums.get(1).getStadiumId(), savedStadiums.get(2).getStadiumId());

        Vote vote = Vote.create(1L, 1L, "연말 행사 장소 투표", endAt);

        Vote savedVote = voteRepository.save(vote);

        List<VoteItemLocate> voteItemLocates = stadiumIds.stream()
            .map(stadiumId -> VoteItemLocate.create(savedVote, stadiumId))
            .toList();

        List<VoteItemLocate> savedVoteItems = voteItemRepository.saveAll(voteItemLocates);

        Choice choice1 = Choice.create(savedMember.getMemberId(), savedVoteItems.get(0).getVoteItemId());
        Choice choice2 = Choice.create(savedMember.getMemberId(), savedVoteItems.get(1).getVoteItemId());

        List<Choice> choices = choiceRepository.saveAll(List.of(choice1, choice2));
        //when
        Long count = choiceRepository.countByVoteItemId(savedVoteItems.get(0).getVoteItemId());

        //then
        Assertions.assertThat(count).isEqualTo(1L);
    }

    @DisplayName("")
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
        List<Long> stadiumIds = List.of(savedStadiums.get(0).getStadiumId(), savedStadiums.get(1).getStadiumId(), savedStadiums.get(2).getStadiumId());

        Vote vote = Vote.create(1L, 1L, "연말 행사 장소 투표", endAt);

        Vote savedVote = voteRepository.save(vote);

        List<VoteItemLocate> voteItemLocates = stadiumIds.stream()
            .map(stadiumId -> VoteItemLocate.create(savedVote, stadiumId))
            .toList();

        List<VoteItemLocate> savedVoteItems = voteItemRepository.saveAll(voteItemLocates);

        Choice choice1 = Choice.create(savedMember.getMemberId(), savedVoteItems.get(0).getVoteItemId());
        Choice choice2 = Choice.create(savedMember.getMemberId(), savedVoteItems.get(1).getVoteItemId());

        List<Choice> choices = choiceRepository.saveAll(List.of(choice1, choice2));

        //when
        List<Choice> findChoices = choiceRepository.findByMemberIdAndVoteId(savedMember.getMemberId(), savedVote.getVoteId());

        //then
        Assertions.assertThat(findChoices).hasSize(2)
            .extracting("memberId", "voteItemId")
            .containsExactlyInAnyOrder(
                Assertions.tuple(savedMember.getMemberId(), savedVoteItems.get(0).getVoteItemId()),
                Assertions.tuple(savedMember.getMemberId(), savedVoteItems.get(1).getVoteItemId())
            );
    }

}