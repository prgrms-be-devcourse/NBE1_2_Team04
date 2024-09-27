package team4.footwithme.member.service.request;

import team4.footwithme.member.domain.*;

public record JoinServiceRequest(
        String email,
        String password,
        String name,
        String phoneNumber,
        LoginProvider loginProvider,
        String snsId,
        Gender gender,
        MemberRole memberRole,
        TermsAgreed termsAgree
) {
    public Member toEntity(){
        return Member.create(email, password, name, phoneNumber, loginProvider, snsId, gender, memberRole, termsAgree);
    }
}
