package team4.footwithme.vote.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static team4.footwithme.vote.domain.VoteStatus.CLOSED;
import static team4.footwithme.vote.domain.VoteStatus.OPENED;

@Table(indexes = {
    @Index(name = "idx_vote_team_id", columnList = "teamId"),
    @Index(name = "idx_vote_member_id", columnList = "memberId")
})
@SQLDelete(sql = "UPDATE vote SET is_deleted = 'TRUE' WHERE vote_id = ?")
@Entity
public class Vote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @NotNull
    private Long memberId;

    @NotNull
    private Long teamId;

    @NotNull
    @Column(length = 50)
    private String title;

    @NotNull
    private LocalDateTime endAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VoteStatus voteStatus;

    @OneToMany(mappedBy = "vote")
    private List<VoteItem> voteItems = new ArrayList<>();

    private Vote(Long memberId, Long teamId, String title, LocalDateTime endAt) {
        this.memberId = memberId;
        this.teamId = teamId;
        this.title = title;
        this.endAt = endAt;
        this.voteStatus = OPENED;
    }

    protected Vote() {
    }

    public static Vote create(Long memberId, Long teamId, String title, LocalDateTime endAt) {
        validateEndAt(endAt);
        return Vote.builder()
            .memberId(memberId)
            .teamId(teamId)
            .title(title)
            .endAt(endAt)
            .build();
    }

    private static void validateEndAt(LocalDateTime endAt) {
        if (endAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("투표 종료일은 현재 시간 이후여야 합니다.");
        }
    }

    public static VoteBuilder builder() {
        return new VoteBuilder();
    }

    public void addChoice(VoteItem voteItem) {
        this.voteItems.add(voteItem);
    }

    public void updateVoteStatusToClose() {
        this.voteStatus = CLOSED;
    }

    public void update(String updateTitle, LocalDateTime updateEndAt, Long memberId) {
        checkWriterFromMemberId(memberId);
        this.title = updateTitle;
        this.endAt = updateEndAt;
    }

    public void checkWriterFromMemberId(Long memberId) {
        if (isNotWriter(memberId)) {
            throw new IllegalArgumentException("투표 작성자가 아닙니다.");
        }
    }

    private boolean isNotWriter(Long memberId) {
        return !this.memberId.equals(memberId);
    }

    public Instant getInstantEndAt() {
        return endAt.atZone(ZoneId.systemDefault()).toInstant();
    }

    public Long getVoteId() {
        return this.voteId;
    }

    public @NotNull Long getMemberId() {
        return this.memberId;
    }

    public @NotNull Long getTeamId() {
        return this.teamId;
    }

    public @NotNull String getTitle() {
        return this.title;
    }

    public @NotNull LocalDateTime getEndAt() {
        return this.endAt;
    }

    public @NotNull VoteStatus getVoteStatus() {
        return this.voteStatus;
    }

    public List<VoteItem> getVoteItems() {
        return this.voteItems;
    }

    public static class VoteBuilder {
        private Long memberId;
        private Long teamId;
        private String title;
        private LocalDateTime endAt;

        VoteBuilder() {
        }

        public VoteBuilder memberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public VoteBuilder teamId(Long teamId) {
            this.teamId = teamId;
            return this;
        }

        public VoteBuilder title(String title) {
            this.title = title;
            return this;
        }

        public VoteBuilder endAt(LocalDateTime endAt) {
            this.endAt = endAt;
            return this;
        }

        public Vote build() {
            return new Vote(this.memberId, this.teamId, this.title, this.endAt);
        }

        public String toString() {
            return "Vote.VoteBuilder(memberId=" + this.memberId + ", teamId=" + this.teamId + ", title=" + this.title + ", endAt=" + this.endAt + ")";
        }
    }
}
