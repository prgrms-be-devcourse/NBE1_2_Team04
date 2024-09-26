package team4.footwithme.vote.service.request;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;

public record VoteCreateServiceRequest (
    String title,
    LocalDateTime endAt,
    List<Long> choices
) {
}
