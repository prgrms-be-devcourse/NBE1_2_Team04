package team4.footwithme.member.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.config.SecurityConfig;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.jwt.JwtTokenUtil;
import team4.footwithme.member.jwt.response.TokenResponse;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.member.service.request.JoinServiceRequest;
import team4.footwithme.member.service.request.LoginServiceRequest;
import team4.footwithme.member.service.response.LoginResponse;
import team4.footwithme.member.service.response.MemberResponse;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final SecurityConfig jwtSecurityConfig;
    private final JwtTokenUtil jwtTokenUtil;
    private final CookieService cookieService;
    private final RedisTemplate redisTemplate;

    @Override
    @Transactional
    public MemberResponse join(JoinServiceRequest serviceRequest) {
        if(memberRepository.existByEmail(serviceRequest.email()))
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");

        Member member = serviceRequest.toEntity();
        member.encodePassword(jwtSecurityConfig.passwordEncoder());
        memberRepository.save(member);

        return MemberResponse.from(member);

    }

    @Override
    public LoginResponse login(LoginServiceRequest serviceRequest, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(serviceRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        if(!jwtSecurityConfig.passwordEncoder().matches(serviceRequest.password(), member.getPassword())) {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }

        TokenResponse tokenResponse = jwtTokenUtil.createToken(member.getEmail());
        redisTemplate.opsForValue().set(member.getEmail(), tokenResponse.refreshToken(), tokenResponse.refreshTokenExpirationTime(), TimeUnit.MICROSECONDS);
        // Redis에 RefreshToken 저장

        cookieService.setHeader(response, tokenResponse.refreshToken());
        // 쿠키에 refreshToken 저장

        return LoginResponse.from(tokenResponse);
    }
}
