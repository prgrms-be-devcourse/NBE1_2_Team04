package team4.footwithme.chat.service.event;

import team4.footwithme.member.domain.Member;

public record TeamMemberLeaveEvent(
    Member member,
    Long teamId
) {
}
