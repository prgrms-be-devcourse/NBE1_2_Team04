package team4.footwithme.chat.service.event;

import team4.footwithme.member.domain.Member;

public record ReservationMemberLeaveEvent(
        Member member,
        Long reservationId
) {
}
