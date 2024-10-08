package team4.footwithme.resevation.service.response;

import team4.footwithme.member.domain.Member;
import team4.footwithme.member.domain.MemberRole;

public record MWParticipantMemberInfo(
        Long memberId,
        String email,
        String name,
        MemberRole memberRole) {

    public MWParticipantMemberInfo(Member member) {
        this(
                member.getMemberId(),
                member.getEmail(),
                member.getName(),
                member.getMemberRole()
        );
    }
}
