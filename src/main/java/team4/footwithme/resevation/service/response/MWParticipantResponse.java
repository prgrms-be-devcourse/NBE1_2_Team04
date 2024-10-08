package team4.footwithme.resevation.service.response;

import team4.footwithme.resevation.domain.Participant;

public record MWParticipantResponse(
        Long participantId,
        Long reservationId,
        MWParticipantMemberInfo memberInfo
        ) {
    public MWParticipantResponse(Participant participant) {
        this(
                participant.getParticipantId(),
                participant.getReservation().getReservationId(),
                new MWParticipantMemberInfo(participant.getMember())
        );
    }
}
