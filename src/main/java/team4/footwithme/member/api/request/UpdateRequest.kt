package team4.footwithme.member.api.request

import jakarta.validation.constraints.Pattern
import team4.footwithme.member.domain.Gender
import team4.footwithme.member.service.request.UpdateServiceRequest

@JvmRecord
data class UpdateRequest(
    val name: String,
    val phoneNumber: @Pattern(
        regexp = "^010-\\d{3,4}-\\d{4}$",
        message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 합니다."
    ) String?,
    val gender: Gender
) {
    fun toServiceRequest(): UpdateServiceRequest {
        return UpdateServiceRequest(name, phoneNumber, gender)
    }
}
