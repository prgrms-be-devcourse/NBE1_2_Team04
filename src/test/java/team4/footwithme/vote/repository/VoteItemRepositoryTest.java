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
import team4.footwithme.vote.domain.Vote;
import team4.footwithme.vote.domain.VoteItem;
import team4.footwithme.vote.domain.VoteItemLocate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class VoteItemRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private VoteItemRepository voteItemRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StadiumRepository stadiumRepository;


    @DisplayName("투표 ID로 장소 투표 조회")
    @Test
    void test() {
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

        //when
        List<VoteItem> findVoteItems = voteItemRepository.findByVoteVoteId(savedVote.getVoteId());

        //then
        Assertions.assertThat(findVoteItems).hasSize(3)
            .extracting("voteItemId","stadiumId","vote.voteId")
            .containsExactlyInAnyOrder(
                Assertions.tuple(savedVoteItems.get(0).getVoteItemId(), savedVoteItems.get(0).getStadiumId(), savedVote.getVoteId()),
                Assertions.tuple(savedVoteItems.get(1).getVoteItemId(), savedVoteItems.get(1).getStadiumId(), savedVote.getVoteId()),
                Assertions.tuple(savedVoteItems.get(2).getVoteItemId(), savedVoteItems.get(2).getStadiumId(), savedVote.getVoteId())
            );

    }

}