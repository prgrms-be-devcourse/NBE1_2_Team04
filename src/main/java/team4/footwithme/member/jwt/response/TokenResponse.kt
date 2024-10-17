package team4.footwithme.member.jwt.response

import lombok.Builder

@JvmRecord
data class TokenResponse(
    @JvmField val accessToken: String?,
    @JvmField val refreshToken: String?,
    @JvmField val refreshTokenExpirationTime: Long?
) {
    companion object {
        fun of(accessToken: String?, refreshToken: String?, refreshTokenExpirationTime: Long?): TokenResponse {
            return TokenResponse(accessToken, refreshToken, refreshTokenExpirationTime)
        }
    }
}
