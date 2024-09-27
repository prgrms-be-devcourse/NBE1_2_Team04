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

    private Long stadiumId;

    @Builder
    private VoteItemLocate(Vote vote, Long stadiumId) {
        super(vote);
        this.stadiumId = stadiumId;
    }

    public static VoteItemLocate create(Vote vote, Long stadiumId) {
        return VoteItemLocate.builder()
            .vote(vote)
            .stadiumId(stadiumId)
            .build();
    }

}
