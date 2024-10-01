package team4.footwithme.vote.service.request;

import java.time.LocalDateTime;

public record VoteUpdateServiceRequest (
    String title,
    LocalDateTime endAt
) {
}
