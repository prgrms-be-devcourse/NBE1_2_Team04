package team4.footwithme.vote.domain;

import jakarta.persistence.*;

@Table(indexes = {
    @Index(name = "idx_choice_member_id", columnList = "memberId"),
    @Index(name = "idx_choice_vote_item_id", columnList = "voteItemId")
})
@Entity
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long choiceId;

    private Long memberId;

    private Long voteItemId;

    private Choice(Long memberId, Long voteItemId) {
        this.memberId = memberId;
        this.voteItemId = voteItemId;
    }

    protected Choice() {
    }

    public static Choice create(Long memberId, Long voteItemId) {
        return Choice.builder()
            .memberId(memberId)
            .voteItemId(voteItemId)
            .build();
    }

    public static ChoiceBuilder builder() {
        return new ChoiceBuilder();
    }

    public Long getChoiceId() {
        return this.choiceId;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public Long getVoteItemId() {
        return this.voteItemId;
    }

    public static class ChoiceBuilder {
        private Long memberId;
        private Long voteItemId;

        ChoiceBuilder() {
        }

        public ChoiceBuilder memberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public ChoiceBuilder voteItemId(Long voteItemId) {
            this.voteItemId = voteItemId;
            return this;
        }

        public Choice build() {
            return new Choice(this.memberId, this.voteItemId);
        }

        public String toString() {
            return "Choice.ChoiceBuilder(memberId=" + this.memberId + ", voteItemId=" + this.voteItemId + ")";
        }
    }
}
