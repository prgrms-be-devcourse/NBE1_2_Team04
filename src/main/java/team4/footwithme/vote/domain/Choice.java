package team4.footwithme.vote.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long choiceId;

    private Long memberId;

    private Long voteItemId;

    @Builder
    private Choice(Long memberId, Long voteItemId) {
        this.memberId = memberId;
        this.voteItemId = voteItemId;
    }

    public static Choice create(Long memberId, Long voteItemId) {
        return Choice.builder()
            .memberId(memberId)
            .voteItemId(voteItemId)
            .build();
    }

}
