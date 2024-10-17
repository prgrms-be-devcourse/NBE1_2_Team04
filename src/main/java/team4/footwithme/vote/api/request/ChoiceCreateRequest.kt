package team4.footwithme.vote.api.request;

import team4.footwithme.vote.service.request.ChoiceCreateServiceRequest;

import java.util.List;

public record ChoiceCreateRequest(
    List<Long> voteItemIds
) {

    public ChoiceCreateServiceRequest toServiceRequest() {
        return new ChoiceCreateServiceRequest(
            voteItemIds
        );
    }
}
