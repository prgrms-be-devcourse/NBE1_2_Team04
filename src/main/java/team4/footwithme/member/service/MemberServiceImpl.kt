package team4.footwithme.member.service

import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team4.footwithme.config.SecurityConfig
import team4.footwithme.member.domain.*
import team4.footwithme.member.jwt.JwtTokenFilter
import team4.footwithme.member.jwt.JwtTokenUtil
import team4.footwithme.member.jwt.response.TokenResponse
import team4.footwithme.member.repository.MemberRepository
import team4.footwithme.member.service.request.JoinServiceRequest
import team4.footwithme.member.service.request.LoginServiceRequest
import team4.footwithme.member.service.request.UpdatePasswordServiceRequest
import team4.footwithme.member.service.request.UpdateServiceRequest
import team4.footwithme.member.service.response.LoginResponse
import team4.footwithme.member.service.response.MemberResponse
import java.util.concurrent.TimeUnit

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val jwtSecurityConfig: SecurityConfig,
    private val jwtTokenUtil: JwtTokenUtil,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val passwordEncoder: BCryptPasswordEncoder
) : MemberService {
    @Transactional
    override fun join(serviceRequest: JoinServiceRequest?): MemberResponse {
        require(!memberRepository.existByEmail(serviceRequest!!.email!!)) { "이미 존재하는 이메일 입니다." }

        val member = serviceRequest.toEntity()

        if (member!!.password != null) { // OAUth 2 회원가입 시 Password 가 null로 들어옴
            member.encodePassword(jwtSecurityConfig.passwordEncoder())
        }

        memberRepository.save(member)

        return MemberResponse.Companion.from(member)
    }

    @Transactional
    override fun login(serviceRequest: LoginServiceRequest?): LoginResponse {
        val member = memberRepository.findByEmail(serviceRequest!!.email)
            .orElseThrow { IllegalArgumentException("존재하지 않는 사용자 입니다.") }

        require(
            jwtSecurityConfig.passwordEncoder().matches(serviceRequest.password, member!!.password)
        ) { "패스워드가 일치하지 않습니다." }

        val tokenResponse = jwtTokenUtil.createToken(member.email)
        redisTemplate.opsForValue().set(member.email, tokenResponse!!.refreshToken,
            tokenResponse!!.refreshTokenExpirationTime!!, TimeUnit.MICROSECONDS)

        // Redis에 RefreshToken 저장
        return LoginResponse.Companion.from(tokenResponse)
    }

    @Transactional
    override fun logout(request: HttpServletRequest): String {
        val accessToken = jwtTokenUtil.resolveToken(request)
        val email = jwtTokenUtil.getEmailFromToken(accessToken)

        jwtTokenUtil.tokenValidation(accessToken)

        if (redisTemplate.opsForValue()[email] != null) {
            redisTemplate.delete(email)
        }

        val expiration = jwtTokenUtil.getExpiration(accessToken)
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MICROSECONDS)

        return "Success Logout"
    }

    override fun reissue(request: HttpServletRequest, refreshToken: String?): TokenResponse {
        var refreshToken = refreshToken
        if (refreshToken == null || refreshToken.isEmpty()) {
            refreshToken = JwtTokenFilter.Companion.getRefreshTokenByRequest(request) // 헤더에 없을 경우 쿠키에서 꺼내 씀
        }

        jwtTokenUtil.tokenValidation(refreshToken)

        val newAccessToken = jwtTokenUtil.reCreateAccessToken(refreshToken)
        val refreshTokenExpirationTime = jwtTokenUtil.getExpiration(refreshToken)

        return TokenResponse.Companion.of(
            newAccessToken,
            refreshToken,
            refreshTokenExpirationTime
        )
    }

    @Transactional
    override fun update(member: Member?, request: UpdateServiceRequest?): MemberResponse {
        member?.update(request!!.name, request.phoneNumber, request.gender)
        memberRepository.save(member)

        return MemberResponse.Companion.from(member)
    }

    @Transactional
    override fun updatePassword(member: Member?, serviceRequest: UpdatePasswordServiceRequest?): String {
        require(
            jwtSecurityConfig.passwordEncoder().matches(serviceRequest!!.prePassword, member?.password)
        ) { "이전 패스워드가 일치하지 않습니다." }
        member?.changePassword(passwordEncoder.encode(serviceRequest.newPassword))
        memberRepository.save(member)

        return "Success Change Password"
    }
}
