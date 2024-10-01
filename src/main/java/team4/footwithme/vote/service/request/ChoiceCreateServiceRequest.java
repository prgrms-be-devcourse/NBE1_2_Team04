package team4.footwithme.vote.service.request;

import java.util.List;

public record ChoiceCreateServiceRequest(
    List<Long> voteItemIds
) {
}
