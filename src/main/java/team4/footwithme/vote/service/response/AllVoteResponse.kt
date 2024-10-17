package team4.footwithme.vote.service.response;

import team4.footwithme.vote.domain.Vote;

import java.time.LocalDateTime;

public record AllVoteResponse(
    Long voteId,
    String title,
    LocalDateTime endAt,
    String status
) {
    public static AllVoteResponse from(Vote vote) {
        return new AllVoteResponse(
            vote.getVoteId(),
            vote.getTitle(),
            vote.getEndAt(),
            vote.getVoteStatus().getText()
        );
    }
}
