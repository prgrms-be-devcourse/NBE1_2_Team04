package team4.footwithme.vote.service.response;

import team4.footwithme.vote.domain.Vote;

import java.time.LocalDateTime;
import java.util.List;

public record VoteResponse(
    Long voteId,
    String title,
    LocalDateTime endAt,
    String voteStatus,
    List<VoteItemResponse> choices
) {

    public static VoteResponse of(Vote vote, List<VoteItemResponse> choices) {
        return new VoteResponse(
            vote.getVoteId(),
            vote.getTitle(),
            vote.getEndAt(),
            vote.getVoteStatus().getText(),
            choices
        );
    }
}
