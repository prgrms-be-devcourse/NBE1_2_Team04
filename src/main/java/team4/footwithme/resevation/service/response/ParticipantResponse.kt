package team4.footwithme.resevation.service.response;

import team4.footwithme.resevation.domain.Participant;
import team4.footwithme.resevation.domain.ParticipantRole;

public record ParticipantResponse(
    Long participantId,
    Long reservationId,
    ParticipantRole role,
    ParticipantMemberInfo memberInfo
) {
    public ParticipantResponse(Participant participant) {
        this(
            participant.getParticipantId(),
            participant.getReservation().getReservationId(),
            participant.getParticipantRole(),
            new ParticipantMemberInfo(participant.getMember())
        );
    }
}
