package team4.footwithme.member.api.request;

import jakarta.validation.constraints.NotNull;
import team4.footwithme.member.service.request.LoginServiceRequest;

public record LoginRequest (
        @NotNull(message = "이메일 입력하셔야 합니다.")
        String email,
        @NotNull(message = "비밀번호를 입력하셔야 합니다.")
        String password
){
    public LoginServiceRequest toServiceRequest(){
        return new LoginServiceRequest(email, password);
    }
}
