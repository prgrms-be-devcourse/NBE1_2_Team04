package team4.footwithme.member.service

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import team4.footwithme.member.jwt.JwtTokenUtil

@Component
class CookieService {
    fun setHeader(response: HttpServletResponse, refreshToken: String?) {
        if (refreshToken != null) {
            response.addHeader(JwtTokenUtil.Companion.REFRESH_TOKEN, refreshToken)
            response.addHeader("Set-Cookie", createRefreshToken(refreshToken).toString())
        }
    }

    companion object {
        fun createRefreshToken(refreshToken: String?): ResponseCookie {
            return ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .maxAge((14 * 24 * 60 * 60 * 1000).toLong())
                .httpOnly(true)
                .build()
        }
    }
}
