package team4.footwithme.resevation.service.response;

import team4.footwithme.resevation.domain.Participant;
import team4.footwithme.resevation.domain.ParticipantRole;

// TODO :: ParticipantInfoResponse 로 변경 부탁
public record ParticipantInfoResponse(
        String memberName,
        ParticipantRole role
) {
    public static ParticipantInfoResponse from(Participant participant) {
        return new ParticipantInfoResponse(
            participant.getMember().getName(),
            participant.getParticipantRole()
        );
    }
}
