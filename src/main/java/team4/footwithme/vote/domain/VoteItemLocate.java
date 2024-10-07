package team4.footwithme.vote.domain;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class VoteItemLocate extends VoteItem {

    private Long courtId;

    @Builder
    private VoteItemLocate(Vote vote, Long courtId) {
        super(vote);
        this.courtId = courtId;
    }

    public static VoteItemLocate create(Vote vote, Long courtId) {
        return VoteItemLocate.builder()
            .vote(vote)
            .courtId(courtId)
            .build();
    }

}
