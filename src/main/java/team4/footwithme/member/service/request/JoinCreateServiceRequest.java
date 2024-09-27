package team4.footwithme.member.service.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import team4.footwithme.member.domain.*;

public record JoinCreateServiceRequest(
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
