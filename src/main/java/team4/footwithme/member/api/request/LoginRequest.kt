package team4.footwithme.member.api.request

import jakarta.validation.constraints.NotNull
import team4.footwithme.member.service.request.LoginServiceRequest

@JvmRecord
data class LoginRequest(
    val email: @NotNull(message = "이메일 입력하셔야 합니다.") String?,
    val password: @NotNull(message = "비밀번호를 입력하셔야 합니다.") String?
) {
    fun toServiceRequest(): LoginServiceRequest {
        return LoginServiceRequest(email, password)
    }
}
