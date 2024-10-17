package team4.footwithme.member.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.ObjectUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.*

@Component
class JwtTokenFilter(private val jwtTokenUtil: JwtTokenUtil, private val redisTemplate: RedisTemplate<*, *>) :
    OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val cookie_refreshToken = getRefreshTokenByRequest(request)
        val accessToken = jwtTokenUtil.getHeaderToken(request, ACCESS_TOKEN)
        val refreshToken = jwtTokenUtil.getHeaderToken(request, REFRESH_TOKEN)
        if (cookie_refreshToken != null) {
            processSecurity(accessToken, cookie_refreshToken, response)
        }

        if (cookie_refreshToken == null) {
            processSecurity(accessToken, refreshToken, response)
        }

        filterChain.doFilter(request, response)
    }

    @Throws(ServletException::class)
    private fun processSecurity(accessToken: String?, refreshToken: String?, response: HttpServletResponse) {
        if (accessToken != null) {
            jwtTokenUtil.tokenValidation(accessToken)

            val isLogout = redisTemplate.opsForValue()[accessToken] as String

            if (ObjectUtils.isEmpty(isLogout)) {
                setAuthentication(jwtTokenUtil.getEmailFromToken(accessToken))
            }
        }
        if (accessToken == null && refreshToken != null) {
            jwtTokenUtil.refreshTokenValidation(refreshToken)
            setAuthentication(jwtTokenUtil.getEmailFromToken(refreshToken))
        }
    }

    fun setAuthentication(email: String?) {
        val authentication = jwtTokenUtil.createAuthentication(email!!)
        SecurityContextHolder.getContext().authentication = authentication
    }


    companion object {
        const val ACCESS_TOKEN: String = "Authorization"
        const val REFRESH_TOKEN: String = "refresh_token"
        private const val COOKIE_REFRESH_TOKEN = "refreshToken"
        fun getRefreshTokenByRequest(request: HttpServletRequest): String? {
            val cookies = request.cookies

            if (cookies != null && cookies.size != 0) {
                return Arrays.stream(cookies)
                    .filter { c: Cookie -> c.name == COOKIE_REFRESH_TOKEN }.findFirst().map { obj: Cookie -> obj.value }
                    .orElse(null)
            }

            return null
        }
    }
}
