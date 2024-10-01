package team4.footwithme.vote.domain;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class VoteItemDate extends VoteItem {

    private LocalDateTime time;

    @Builder
    private VoteItemDate(Vote vote, LocalDateTime time) {
        super(vote);
        validateTime(time, vote.getEndAt());
        this.time = time;
    }

    public static VoteItemDate create(Vote vote, LocalDateTime time) {
        return VoteItemDate.builder()
            .vote(vote)
            .time(time)
            .build();
    }

    private void validateTime(LocalDateTime time, LocalDateTime endAt) {
        if (time.isBefore(endAt)) {
            throw new IllegalArgumentException("투표 종료 시간보다 이른 시간을 선택할 수 없습니다.");
        }
    }
}
