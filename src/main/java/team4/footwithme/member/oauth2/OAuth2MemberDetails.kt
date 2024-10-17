package team4.footwithme.member.oauth2

import team4.footwithme.member.domain.LoginProvider

interface OAuth2MemberDetails {
    val provider: LoginProvider?

    val sNSId: String?

    val email: String?

    val name: String?
}
