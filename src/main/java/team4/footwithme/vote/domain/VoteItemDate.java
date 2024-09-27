package team4.footwithme.vote.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
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
        this.time = time;
    }

    public static VoteItemDate create(Vote vote, LocalDateTime time) {
        return VoteItemDate.builder()
            .vote(vote)
            .time(time)
            .build();
    }
}
