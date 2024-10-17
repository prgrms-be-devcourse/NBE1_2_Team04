package team4.footwithme.vote.service;

import java.time.LocalDateTime;
import java.util.List;

public record EndVoteEvent(
    Long courtId,
    Long memberId,
    Long teamId,
    LocalDateTime matchDate,
    List<Long> memberIds
) {
}
