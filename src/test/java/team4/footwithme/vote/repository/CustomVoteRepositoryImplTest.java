package team4.footwithme.vote.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.vote.domain.Vote;
import team4.footwithme.vote.domain.VoteStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
class CustomVoteRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private VoteRepository voteRepository;

    @DisplayName("삭제되지 않은 투표를 id로 조회한다.")
    @Test
    void findNotDeletedVoteById() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusHours(1);
        Vote vote = Vote.create(1L, 1L, "title", endAt);

        Vote savedVote = voteRepository.save(vote);
        //when
        Optional<Vote> foundVote = voteRepository.findNotDeletedVoteById(savedVote.getVoteId());
        //then
        assertThat(foundVote.get())
            .extracting("voteId", "title", "endAt", "isDeleted")
            .contains(savedVote.getVoteId(), "title", endAt, IsDeleted.FALSE);
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
        Optional<Vote> foundVote = voteRepository.findNotDeletedVoteById(savedVote.getVoteId());
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
            .extracting("voteId", "title", "endAt", "isDeleted","voteStatus")
            .containsExactlyInAnyOrder(
                tuple(vote1.getVoteId(), "title", endAt, IsDeleted.FALSE, VoteStatus.OPENED),
                tuple(vote2.getVoteId(), "title", endAt, IsDeleted.FALSE, VoteStatus.OPENED)
            );
    }

}