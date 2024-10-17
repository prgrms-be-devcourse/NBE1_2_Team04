package team4.footwithme.member.service.request

@JvmRecord
data class UpdatePasswordServiceRequest(
    val prePassword: String,
    val newPassword: String?
)
