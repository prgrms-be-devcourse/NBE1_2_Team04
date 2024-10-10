package team4.footwithme.member.service.response;

import team4.footwithme.member.domain.Gender;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.domain.MemberRole;
import team4.footwithme.member.domain.TermsAgreed;

public record MemberResponse(
    Long memberId,
    String email,
    String name,
    String phoneNumber,
    Gender gender,
    MemberRole memberRole,
    TermsAgreed termsAgreed
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getMemberId(),
            member.getEmail(),
            member.getName(),
            member.getPhoneNumber(),
            member.getGender(),
            member.getMemberRole(),
            member.getTermsAgreed());
    }
}
