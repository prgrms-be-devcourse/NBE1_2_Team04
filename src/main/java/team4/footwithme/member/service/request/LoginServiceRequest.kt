package team4.footwithme.member.service.request

@JvmRecord
data class LoginServiceRequest(
    val email: String?,
    val password: String?
)
