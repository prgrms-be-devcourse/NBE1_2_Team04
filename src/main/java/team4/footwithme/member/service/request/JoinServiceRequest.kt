package team4.footwithme.member.service.request

import team4.footwithme.member.domain.*
import team4.footwithme.member.domain.Member.Companion.create

@JvmRecord
data class JoinServiceRequest(
    @JvmField val email: String?,
    val password: String?,
    @JvmField val name: String?,
    @JvmField val phoneNumber: String?,
    val loginProvider: LoginProvider,
    val snsId: String,
    @JvmField val gender: Gender,
    @JvmField val memberRole: MemberRole,
    @JvmField val termsAgree: TermsAgreed?
) {
    fun toEntity(): Member {
        return create(email, password, name, phoneNumber, loginProvider, snsId, gender, memberRole, termsAgree)
    }
}
