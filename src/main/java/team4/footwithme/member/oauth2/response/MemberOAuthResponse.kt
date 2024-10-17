package team4.footwithme.member.oauth2.response

import team4.footwithme.member.domain.LoginProvider
import team4.footwithme.member.domain.Member

@JvmRecord
data class MemberOAuthResponse(
    val provider: LoginProvider?,
    val snsId: String?,
    val email: String?
) {
    companion object {
        fun from(member: Member?): MemberOAuthResponse {
            return MemberOAuthResponse(
                member!!.loginType!!.loginProvider,
                member.loginType!!.snsId,
                member.email
            )
        }
    }
}
