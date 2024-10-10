package team4.footwithme.resevation.service.response;

import team4.footwithme.member.domain.Member;
import team4.footwithme.member.domain.MemberRole;

public record ParticipantMemberInfo(
    Long memberId,
    String email,
    String name,
    MemberRole memberRole) {

    public ParticipantMemberInfo(Member member) {
        this(
            member.getMemberId(),
            member.getEmail(),
            member.getName(),
            member.getMemberRole()
        );
    }
}
