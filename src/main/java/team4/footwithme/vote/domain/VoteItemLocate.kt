package team4.footwithme.vote.domain;

import jakarta.persistence.Entity;

@Entity
public class VoteItemLocate extends VoteItem {

    private Long courtId;

    private VoteItemLocate(Vote vote, Long courtId) {
        super(vote);
        this.courtId = courtId;
    }

    protected VoteItemLocate() {
    }

    public static VoteItemLocate create(Vote vote, Long courtId) {
        return VoteItemLocate.builder()
            .vote(vote)
            .courtId(courtId)
            .build();
    }

    public static VoteItemLocateBuilder builder() {
        return new VoteItemLocateBuilder();
    }

    public Long getCourtId() {
        return this.courtId;
    }

    public static class VoteItemLocateBuilder {
        private Vote vote;
        private Long courtId;

        VoteItemLocateBuilder() {
        }

        public VoteItemLocateBuilder vote(Vote vote) {
            this.vote = vote;
            return this;
        }

        public VoteItemLocateBuilder courtId(Long courtId) {
            this.courtId = courtId;
            return this;
        }

        public VoteItemLocate build() {
            return new VoteItemLocate(this.vote, this.courtId);
        }

        public String toString() {
            return "VoteItemLocate.VoteItemLocateBuilder(vote=" + this.vote + ", courtId=" + this.courtId + ")";
        }
    }
}
