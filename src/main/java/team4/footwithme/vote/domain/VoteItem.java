package team4.footwithme.vote.domain;

import jakarta.persistence.*;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@Entity
public class VoteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;

    public VoteItem(Vote vote) {
        this.vote = vote;
        vote.addChoice(this);
    }

    protected VoteItem() {
    }

    public Long getVoteItemId() {
        return this.voteItemId;
    }

    public Vote getVote() {
        return this.vote;
    }
}
