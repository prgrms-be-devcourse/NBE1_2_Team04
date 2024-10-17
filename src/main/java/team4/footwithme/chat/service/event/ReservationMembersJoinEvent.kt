package team4.footwithme.chat.service.event;

import team4.footwithme.resevation.domain.Participant;

import java.util.List;

public record ReservationMembersJoinEvent(
    List<Participant> members,
    Long reservationId
) {
}
