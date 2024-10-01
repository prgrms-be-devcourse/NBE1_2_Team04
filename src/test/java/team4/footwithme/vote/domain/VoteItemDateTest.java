package team4.footwithme.vote.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class VoteItemDateTest {

    @DisplayName("투표 상세 항목 생성")
    @Test
    void createVoteItem() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        Vote vote = Vote.create(1L, 1L, "투표 제목", endAt);

        LocalDateTime voteItem = LocalDateTime.now().plusDays(2);
        //when
        VoteItemDate voteItemDate = VoteItemDate.create(vote, voteItem);

        //then
        Assertions.assertThat(voteItemDate).isNotNull()
            .extracting("time")
            .isEqualTo(voteItem);
    }

    @DisplayName("")
    @Test
    void createVoteItemWhenVoteItemIsBeforeEndAtThrowException() {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(2);
        Vote vote = Vote.create(1L, 1L, "투표 제목", endAt);
        //when
        //then
        Assertions.assertThatIllegalArgumentException().isThrownBy(()->VoteItemDate.create(vote, endAt.minusDays(1)))
            .withMessage("투표 종료 시간보다 이른 시간을 선택할 수 없습니다.");
    }

}