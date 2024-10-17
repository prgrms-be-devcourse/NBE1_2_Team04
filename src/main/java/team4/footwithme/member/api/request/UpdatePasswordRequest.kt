package team4.footwithme.member.api.request

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import team4.footwithme.member.api.request.validation.PasswordMatches
import team4.footwithme.member.service.request.UpdatePasswordServiceRequest

@PasswordMatches(passwordField = "newPassword", passwordConfirmField = "newPasswordConfirm")
@JvmRecord
data class UpdatePasswordRequest(
    val prePassword: String,
    @JvmField val newPassword: @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,16}$",
        message = "비밀번호는 8~16자 사이  숫자, 영문자, 특수 문자를 각각 최소 한 개 이상 포함하여야 합니다."
    ) @NotNull(message = "새 비밀번호를 입력하셔야 합니다.") String?,
    val newPasswordConfirm: @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,16}$",
        message = "비밀번호는 8~16자 사이  숫자, 영문자, 특수 문자를 각각 최소 한 개 이상 포함하여야 합니다."
    ) @NotNull(message = "새 비밀번호 확인을 입력하셔야 합니다.") String?
) {
    fun toServiceRequest(): UpdatePasswordServiceRequest {
        return UpdatePasswordServiceRequest(prePassword, newPassword)
    }
}
