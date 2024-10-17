package team4.footwithme.vote.domain;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class VoteItemDate extends VoteItem {

    private LocalDateTime time;

    private VoteItemDate(Vote vote, LocalDateTime time) {
        super(vote);
        validateTime(time, vote.getEndAt());
        this.time = time;
    }

    protected VoteItemDate() {
    }

    public static VoteItemDate create(Vote vote, LocalDateTime time) {
        return VoteItemDate.builder()
            .vote(vote)
            .time(time)
            .build();
    }

    public static VoteItemDateBuilder builder() {
        return new VoteItemDateBuilder();
    }

    private void validateTime(LocalDateTime time, LocalDateTime endAt) {
        if (time.isBefore(endAt)) {
            throw new IllegalArgumentException("투표 종료 시간보다 이른 시간을 선택할 수 없습니다.");
        }
    }

    public LocalDateTime getTime() {
        return this.time;
    }

    public static class VoteItemDateBuilder {
        private Vote vote;
        private LocalDateTime time;

        VoteItemDateBuilder() {
        }

        public VoteItemDateBuilder vote(Vote vote) {
            this.vote = vote;
            return this;
        }

        public VoteItemDateBuilder time(LocalDateTime time) {
            this.time = time;
            return this;
        }

        public VoteItemDate build() {
            return new VoteItemDate(this.vote, this.time);
        }

        public String toString() {
            return "VoteItemDate.VoteItemDateBuilder(vote=" + this.vote + ", time=" + this.time + ")";
        }
    }
}
