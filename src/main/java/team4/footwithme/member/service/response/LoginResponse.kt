package team4.footwithme.member.service.response

import team4.footwithme.member.jwt.response.TokenResponse

@JvmRecord
data class LoginResponse(
    @JvmField val accessToken: String?,
    @JvmField val refreshToken: String?
) {
    companion object {
        fun from(tokenResponse: TokenResponse?): LoginResponse {
            return LoginResponse(tokenResponse!!.accessToken, tokenResponse.refreshToken)
        }
    }
}
