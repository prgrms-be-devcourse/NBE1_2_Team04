package team4.footwithme.member.oauth2

import team4.footwithme.member.domain.LoginProvider

class OAuth2GoogleMemberDetails(private val attributes: Map<String, Any>) : OAuth2MemberDetails {
    override val provider: LoginProvider?
        get() = LoginProvider.GOOGLE

    override val sNSId: String?
        get() = attributes["sub"] as String?

    override val email: String?
        get() = attributes["email"] as String?

    override val name: String?
        get() = attributes["name"] as String?
}
