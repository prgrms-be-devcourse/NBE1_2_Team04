package team4.footwithme.member.service.request

import team4.footwithme.member.domain.Gender

@JvmRecord
data class UpdateServiceRequest(
    val name: String,
    val phoneNumber: String?,
    val gender: Gender
)
