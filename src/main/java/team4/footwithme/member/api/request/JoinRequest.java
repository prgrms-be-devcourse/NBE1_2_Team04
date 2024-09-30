package team4.footwithme.member.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import team4.footwithme.member.domain.Gender;
import team4.footwithme.member.domain.LoginProvider;
import team4.footwithme.member.domain.MemberRole;
import team4.footwithme.member.domain.TermsAgreed;
import team4.footwithme.member.service.request.JoinServiceRequest;

public record JoinRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "형식이 이메일이어야 합니다.")
        String email,
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,16}$",
                 message = "비밀번호는 8~16자 사이  숫자, 영문자, 특수 문자를 각각 최소 한 개 이상 포함하여야 합니다.")
        String password,
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,16}$",
                message = "비밀번호는 8~16자 사이  숫자, 영문자, 특수 문자를 각각 최소 한 개 이상 포함하여야 합니다.")
        String passwordConfirm,
        @NotBlank(message = "이름이 빈 칸 입니다.")
        String name,
        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
                message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 합니다.")
        String phoneNumber,
        LoginProvider loginProvider,
        String snsId,
        Gender gender,
        MemberRole memberRole,
        @NotNull(message = "체크해야 합니다.")
        TermsAgreed termsAgree
)
{
        public JoinServiceRequest toServiceRequest() {
                checkPassword();
                return new JoinServiceRequest(
                        email,
                        password,
                        name,
                        phoneNumber,
                        loginProvider,
                        snsId,
                        gender,
                        memberRole,
                        termsAgree
                );

        }

        public void checkPassword(){
                if(!password.equals(passwordConfirm))
                        throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }
}
