package team4.footwithme.chat.service.response;

import team4.footwithme.member.domain.Member;
import team4.footwithme.member.domain.MemberRole;

public record ChatMemberInfo(
        Long memberId,
        String email,
        String name,
        MemberRole memberRole) {

    public ChatMemberInfo(Member member) {
        this(
                member.getMemberId(),
                member.getEmail(),
                member.getName(),
                member.getMemberRole()
        );
    }
}
