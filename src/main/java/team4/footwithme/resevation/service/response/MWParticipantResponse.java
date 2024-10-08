package team4.footwithme.resevation.service.response;

import team4.footwithme.resevation.domain.Participant;
import team4.footwithme.resevation.domain.ParticipantRole;

public record MWParticipantResponse(
        Long participantId,
        Long reservationId,
        ParticipantRole role,
        MWParticipantMemberInfo memberInfo
        ) {
    public MWParticipantResponse(Participant participant) {
        this(
                participant.getParticipantId(),
                participant.getReservation().getReservationId(),
                participant.getParticipantRole(),
                new MWParticipantMemberInfo(participant.getMember())
        );
    }
}
