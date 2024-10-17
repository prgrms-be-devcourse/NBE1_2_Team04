package team4.footwithme.member.oauth2

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.member.domain.MemberRole
import team4.footwithme.member.jwt.JwtTokenUtil
import team4.footwithme.member.oauth2.response.MemberOAuthResponse
import team4.footwithme.member.service.CookieService
import java.io.IOException
import java.util.concurrent.TimeUnit

@Component
class CustomOAuth2LoginSuccessHandler(
    private val jwtTokenUtil: JwtTokenUtil,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val cookieService: CookieService
) : SimpleUrlAuthenticationSuccessHandler() {
    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        // OAuth2User로 캐스팅하여 인증된 사용자 정보를 가져온다.

        val oAuth2User = authentication.principal as CustomOAuth2UserDetails
        // 사용자를 가져온다.
        val member = oAuth2User.member

        val objectMapper = ObjectMapper()
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"

        // 최초 로그인인 경우
        if (member!!.memberRole == MemberRole.GUEST) {
            val apiResponse = ApiResponse.of<MemberOAuthResponse>(
                HttpStatus.OK,
                MemberOAuthResponse.Companion.from(member)
            )
            val jsonResponse = objectMapper.writeValueAsString(apiResponse)
            response.writer.write(jsonResponse)
            return
        }
        // 회원 가입 기록이 있으면
        val tokenResponse = jwtTokenUtil.createToken(member.email)

        val apiResponse = ApiResponse.of(
            HttpStatus.OK,
            tokenResponse
        )

        // Redis에 RefreshToken 저장
        redisTemplate.opsForValue().set(member!!.email, tokenResponse!!.refreshToken, tokenResponse!!.refreshTokenExpirationTime!!, TimeUnit.MICROSECONDS)
        cookieService.setHeader(response, tokenResponse.refreshToken) // 쿠키에 refreshToken 저장
        val jsonResponse = objectMapper.writeValueAsString(apiResponse)
        response.writer.write(jsonResponse)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CustomOAuth2LoginSuccessHandler::class.java)
    }
}
