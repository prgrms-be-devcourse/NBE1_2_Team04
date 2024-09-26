package team4.footwithme.vote.service.response;

public record VoteItemResponse(
    Long voteItemId,
    String content,
    Long voteCount
) {
    public static VoteItemResponse of(Long voteItemId, String contents, Long voteCount) {
        return new VoteItemResponse(
                voteItemId,
                contents,
                voteCount
        );
    }
}
