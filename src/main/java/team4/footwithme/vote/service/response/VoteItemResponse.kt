package team4.footwithme.vote.service.response;

import java.util.List;

public record VoteItemResponse(
    Long voteItemId,
    String content,
    List<Long> memberIds
) {
    public static VoteItemResponse of(Long voteItemId, String contents, List<Long> memberIds) {
        return new VoteItemResponse(
            voteItemId,
            contents,
            memberIds
        );
    }
}
