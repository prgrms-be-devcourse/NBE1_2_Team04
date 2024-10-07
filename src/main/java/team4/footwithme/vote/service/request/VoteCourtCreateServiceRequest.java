package team4.footwithme.vote.service.request;

import java.time.LocalDateTime;
import java.util.List;

public record VoteCourtCreateServiceRequest(
    String title,
    LocalDateTime endAt,
    List<Long> courtIds
) {
}
