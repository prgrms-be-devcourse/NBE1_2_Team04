package team4.footwithme.vote.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.member.domain.*;
import team4.footwithme.vote.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
class CustomVoteRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private VoteItemRepository voteItemRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    @DisplayName("삭제되지 않은 투표를 id로 조회한다.")
    @Test
    void findNotDeletedVoteById() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusHours(1);
        Vote vote = Vote.create(1L, 1L, "title", endAt);

        Vote savedVote = voteRepository.save(vote);
        //when
        Optional<Vote> foundVote = voteRepository.findNotDeletedVoteById(savedVote.voteId);
        //then
        assertThat(foundVote.get())
            .extracting("voteId", "title", "endAt", "isDeleted")
            .contains(savedVote.voteId, "title", endAt, IsDeleted.FALSE);
    }

    @DisplayName("삭제되지 않은 투표를 id로 조회할 때 투표가 없으면 null을 반환한다.")
    @Test
    void findNotDeletedVoteByIdWhenNotFoundThenVoteIsNull() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusHours(1);
        Vote vote = Vote.create(1L, 1L, "title", endAt);

        Vote savedVote = voteRepository.save(vote);
        voteRepository.delete(savedVote);
        //when
        Optional<Vote> foundVote = voteRepository.findNotDeletedVoteById(savedVote.voteId);
        //then
        assertThat(foundVote).isEmpty();
    }

    @DisplayName("삭제되지 않은 투표 중 OPENED 상태인 투표를 조회한다.")
    @Test
    void findOpenedVote() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusHours(1);
        Vote vote1 = Vote.create(1L, 1L, "title", endAt);
        Vote vote2 = Vote.create(3L, 2L, "title", endAt);
        voteRepository.saveAll(List.of(vote1, vote2));

        //when
        List<Vote> savedVotes = voteRepository.findOpenedVotes();
        //then
        assertThat(savedVotes)
            .extracting("voteId", "title", "endAt", "isDeleted", "voteStatus")
            .containsExactlyInAnyOrder(
                tuple(vote1.voteId, "title", endAt, IsDeleted.FALSE, VoteStatus.OPENED),
                tuple(vote2.voteId, "title", endAt, IsDeleted.FALSE, VoteStatus.OPENED)
            );
    }

    @DisplayName("투표 id로 투표에 참여한 총 인원을 계산한다.")
    @Test
    void choiceMemberCountByVoteId() {
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
        Choice choice3 = Choice.create(2L, savedVoteItems.get(2).voteItemId);
        Choice choice4 = Choice.create(3L, savedVoteItems.get(0).voteItemId);
        Choice choice5 = Choice.create(3L, savedVoteItems.get(1).voteItemId);
        Choice choice6 = Choice.create(3L, savedVoteItems.get(2).voteItemId);

        choiceRepository.saveAll(List.of(choice1, choice2, choice3, choice4, choice5, choice6));
        //when
        Long count = voteRepository.choiceMemberCountByVoteId(savedVote.voteId);
        //then
        assertThat(count).isEqualTo(3);
    }

    @DisplayName("전체 투표 목록 조회")
    @Test
    void findAllByTeamId() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusHours(1);
        Vote vote1 = Vote.create(1L, 1L, "title", endAt);
        Vote vote2 = Vote.create(1L, 1L, "제목", endAt);

        voteRepository.saveAll(List.of(vote1, vote2));
        //when
        List<Vote> findVotes = voteRepository.findAllByTeamId(1L);
        //then
        assertThat(findVotes).hasSize(2)
            .extracting("voteId", "title", "endAt", "isDeleted")
            .containsExactlyInAnyOrder(
                tuple(vote1.voteId, "title", endAt, IsDeleted.FALSE),
                tuple(vote2.voteId, "제목", endAt, IsDeleted.FALSE)
            );
    }

}