package team4.footwithme.resevation.api.request;

import jakarta.validation.constraints.NotNull;
import team4.footwithme.resevation.domain.ParticipantRole;
import team4.footwithme.resevation.service.request.ParticipantUpdateServiceRequest;

public record ParticipantUpdateRequest(
        @NotNull
        Long participantId,
        @NotNull
        ParticipantRole role
) {
    public ParticipantUpdateServiceRequest toServiceResponse() {
        return new ParticipantUpdateServiceRequest(participantId, role);
    }
}
