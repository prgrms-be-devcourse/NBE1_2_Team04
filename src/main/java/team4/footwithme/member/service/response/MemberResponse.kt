package team4.footwithme.member.service.response

import team4.footwithme.member.domain.Gender
import team4.footwithme.member.domain.Member
import team4.footwithme.member.domain.MemberRole
import team4.footwithme.member.domain.TermsAgreed

@JvmRecord
data class MemberResponse(
    val memberId: Long?,
    val email: String?,
    val name: String?,
    val phoneNumber: String?,
    val gender: Gender?,
    val memberRole: MemberRole?,
    val termsAgreed: TermsAgreed?
) {
    companion object {
        fun from(member: Member?): MemberResponse {
            return MemberResponse(
                member!!.memberId,
                member.email,
                member.name,
                member.phoneNumber,
                member.gender,
                member.memberRole,
                member.termsAgreed
            )
        }
    }
}
