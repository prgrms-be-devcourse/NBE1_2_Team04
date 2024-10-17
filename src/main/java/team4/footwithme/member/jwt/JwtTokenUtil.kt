package team4.footwithme.member.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import team4.footwithme.member.domain.MemberRole
import team4.footwithme.member.jwt.response.TokenResponse
import team4.footwithme.member.repository.MemberRepository
import java.security.Key
import java.time.Duration
import java.util.*

@Slf4j
@Component
@RequiredArgsConstructor
class JwtTokenUtil {
    private val userDetailService: PrincipalDetailsService? = null
    private val memberRepository: MemberRepository? = null
    private val redisTemplate: RedisTemplate<*, *>? = null

    @Value("\${jwt.secret}")
    private val secretKey: String? = null
    private var key: Key? = null

    @PostConstruct
    fun init() {
        val bytes = Base64.getDecoder().decode(secretKey)
        key = Keys.hmacShaKeyFor(bytes)
    }

    fun getHeaderToken(request: HttpServletRequest, type: String): String? {
        if (type == ACCESS_TOKEN) return resolveToken(request)

        return request.getHeader(REFRESH_TOKEN)
    }

    fun createToken(email: String?): TokenResponse {
        val role = getRoleFromEmail(email)

        val accessToken = createAccessToken(email, role)
        val refreshToken = createRefreshToken(email, role)

        return TokenResponse.Companion.of(accessToken, refreshToken, REFRESH_TIME)
    }

    fun createAccessToken(email: String?, role: MemberRole?): String {
        val date = Date()


        return Jwts.builder()
            .setSubject(email)
            .claim("role", role!!.name)
            .setExpiration(Date(date.time + ACCESS_TIME))
            .setIssuedAt(date)
            .signWith(SignatureAlgorithm.HS256, key)
            .compact()
    }

    fun reCreateAccessToken(refreshToken: String?): String {
        val date = Date()
        val email = getEmailFromToken(refreshToken)
        val role = getRoleFromEmail(email)

        return Jwts.builder()
            .setSubject(email)
            .claim("role", role!!.name)
            .setExpiration(Date(date.time + ACCESS_TIME))
            .setIssuedAt(date)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun createRefreshToken(email: String?, role: MemberRole?): String {
        val date = Date()

        val refreshToken = Jwts.builder()
            .setSubject(email)
            .setExpiration(Date(date.time + REFRESH_TIME))
            .setIssuedAt(date)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        return refreshToken
    }

    fun createAuthentication(email: String): Authentication {
        val userDetails = userDetailService!!.loadUserByUsername(email)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails!!.authorities)
    }

    fun getExpiration(accessToken: String?): Long {
        val expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body.expiration

        val now = Date().time
        return (expiration.time - now)
    }

    private fun getRoleFromEmail(email: String?): MemberRole? {
        val member = memberRepository!!.findByEmail(email)
            .orElseThrow { IllegalArgumentException("존재하지 않는 사용자 입니다.") }

        return member!!.memberRole
    }

    fun getEmailFromToken(token: String?): String {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.subject
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val token = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (StringUtils.hasText(token)) {
            if (token.startsWith(BEARER_PREFIX)) {
                return token.substring(7).trim { it <= ' ' }
            }
            return token
        }

        return null
    }

    fun tokenValidation(token: String?) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
        } catch (e: SecurityException) {
            throw JwtException("유효하지 않은 JWT 토큰입니다.")
        } catch (e: MalformedJwtException) {
            throw JwtException("유효하지 않은 JWT 토큰입니다.")
        } catch (e: ExpiredJwtException) {
            throw JwtException("만료된 JWT 입니다.")
        } catch (e: UnsupportedJwtException) {
            throw JwtException("지원하지 않은 JWT 입니다.")
        } catch (e: IllegalArgumentException) {
            throw JwtException("JWT 값이 비어있습니다.")
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }

    fun refreshTokenValidation(refreshToken: String?) {
        tokenValidation(refreshToken)
        val email = getEmailFromToken(refreshToken)
        var redisRefreshToken: String? = null

        val redisRefresh = redisTemplate!!.opsForValue()[email]

        if (redisRefresh != null) redisRefreshToken = redisRefresh.toString()

        if (redisRefreshToken == null) throw JwtException("유효하지 않은 JWT 토큰입니다.")
    }

    companion object {
        const val ACCESS_TOKEN: String = "Authorization"
        const val REFRESH_TOKEN: String = "refresh_token"
        const val BEARER_PREFIX: String = "Bearer "
        val ACCESS_TIME: Long = Duration.ofMinutes(30).toMillis() // 만료시간 30분
        val REFRESH_TIME: Long = Duration.ofDays(14).toMillis() // 만료시간 2주
    }
}
