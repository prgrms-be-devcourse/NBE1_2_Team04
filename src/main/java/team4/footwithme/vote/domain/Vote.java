package team4.footwithme.vote.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.global.domain.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static team4.footwithme.vote.domain.VoteStatus.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public void delete(Long memberId) {
        checkWriterFrom(memberId);
    }

    public void updateVoteStatusToClose() {
        this.voteStatus = CLOSED;
    }

    public void update(String updateTitle, LocalDateTime updateEndAt, Long memberId) {
        checkWriterFrom(memberId);
        this.title = updateTitle;
        this.endAt = updateEndAt;
    }

    private void checkWriterFrom(Long memberId) {
        if (!isWriter(memberId)) {
            throw new IllegalArgumentException("투표 작성자만 수정,삭제할 수 있습니다.");
        }
    }

    private boolean isWriter(Long memberId) {
        return this.memberId.equals(memberId);
    }
}
