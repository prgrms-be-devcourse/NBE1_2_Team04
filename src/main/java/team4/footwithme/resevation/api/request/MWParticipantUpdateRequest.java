package team4.footwithme.resevation.api.request;

import jakarta.validation.constraints.NotNull;
import team4.footwithme.resevation.domain.ParticipantRole;
import team4.footwithme.resevation.service.request.MWParticipantUpdateServiceRequest;

public record MWParticipantUpdateRequest(
        @NotNull
        Long participantId,
        @NotNull
        ParticipantRole role
) {
    public MWParticipantUpdateServiceRequest toServiceResponse() {
        return new MWParticipantUpdateServiceRequest(participantId, role);
    }
}
