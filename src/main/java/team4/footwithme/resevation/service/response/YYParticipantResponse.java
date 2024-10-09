package team4.footwithme.resevation.service.response;

import team4.footwithme.resevation.domain.Participant;
import team4.footwithme.resevation.domain.ParticipantRole;

public record YYParticipantResponse(
        String memberName,
        ParticipantRole role
) {
    public static YYParticipantResponse from(Participant participant) {
        return new YYParticipantResponse(
            participant.getMember().getName(),
            participant.getParticipantRole()
        );
    }
}
