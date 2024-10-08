package team4.footwithme.resevation.service.request;

import team4.footwithme.resevation.domain.ParticipantRole;

public record MWParticipantUpdateServiceResponse(
        Long participantId,
        ParticipantRole role
) {
}
