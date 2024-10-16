package team4.footwithme.vote.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(indexes = {
    @Index(name = "idx_choice_member_id", columnList = "memberId"),
    @Index(name = "idx_choice_vote_item_id", columnList = "voteItemId")
})
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
