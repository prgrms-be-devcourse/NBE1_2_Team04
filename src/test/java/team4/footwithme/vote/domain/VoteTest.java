package team4.footwithme.vote.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class VoteTest {

    @DisplayName("투표 생성")
    @Test
    void createVote() {
        //given
        LocalDateTime tomorrowDateTime = LocalDateTime.now().plusDays(1);

        //when
        Vote vote = Vote.create(1L, 1L, "투표 제목", tomorrowDateTime);

        //then
        Assertions.assertThat(vote).isNotNull();
    }


    @DisplayName("투표 생성시 현재의 시간보다 이전의 시간이 들어오면 예외가 발생한다.")
    @Test
    void createVoteWhenPastDateTimeIsThrownException() {
        //given
        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);

        //when
        //then
        Assertions.assertThatThrownBy(()->Vote.create(1L,1L,"투표 제목",pastDateTime))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("투표 종료일은 현재 시간 이후여야 합니다.");
    }
}