package team4.footwithme.vote.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.CourtRepository;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.vote.domain.Vote;
import team4.footwithme.vote.domain.VoteItem;
import team4.footwithme.vote.domain.VoteItemLocate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @Autowired
    private CourtRepository courtRepository;


    @DisplayName("투표 ID로 장소 투표 조회")
    @Test
    void findS() {
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

        List<Long> courtIds = List.of(savedCourts.get(0).courtId, savedCourts.get(1).courtId, savedCourts.get(2).courtId);

        Vote vote = Vote.create(1L, 1L, "연말 행사 장소 투표", endAt);

        Vote savedVote = voteRepository.save(vote);

        List<VoteItemLocate> voteItemLocates = courtIds.stream()
            .map(courtId -> VoteItemLocate.create(savedVote, courtId))
            .toList();

        List<VoteItemLocate> savedVoteItems = voteItemRepository.saveAll(voteItemLocates);

        //when
        List<VoteItem> findVoteItems = voteItemRepository.findByVoteVoteId(savedVote.voteId);

        //then
        Assertions.assertThat(findVoteItems).hasSize(3)
            .extracting("voteItemId", "courtId", "vote.voteId")
            .containsExactlyInAnyOrder(
                Assertions.tuple(savedVoteItems.get(0).voteItemId, savedVoteItems.get(0).getCourtId(), savedVote.voteId),
                Assertions.tuple(savedVoteItems.get(1).voteItemId, savedVoteItems.get(1).getCourtId(), savedVote.voteId),
                Assertions.tuple(savedVoteItems.get(2).voteItemId, savedVoteItems.get(2).getCourtId(), savedVote.voteId)
            );

    }

}