package team4.footwithme.vote.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static team4.footwithme.vote.domain.VoteStatus.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    private Vote(Long memberId, Long teamId, String title, LocalDateTime endAt) {
        this.memberId = memberId;
        this.teamId = teamId;
        this.title = title;
        this.endAt = endAt;
        this.voteStatus = OPENED;
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
}
