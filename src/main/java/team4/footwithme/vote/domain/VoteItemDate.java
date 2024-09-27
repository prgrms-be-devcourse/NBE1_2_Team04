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

    @NotNull
    private LocalDateTime endAt;


    @Builder
    private VoteItemDate(Vote vote, LocalDateTime endAt) {
        super(vote);
        this.endAt = endAt;
    }

}
