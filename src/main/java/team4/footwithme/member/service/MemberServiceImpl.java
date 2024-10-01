package team4.footwithme.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.config.SecurityConfig;
import team4.footwithme.member.api.request.UpdateRequest;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.jwt.JwtTokenFilter;
import team4.footwithme.member.jwt.JwtTokenUtil;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.member.jwt.response.TokenResponse;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.member.service.request.JoinServiceRequest;
import team4.footwithme.member.service.request.LoginServiceRequest;
import team4.footwithme.member.service.request.UpdatePasswordServiceRequest;
import team4.footwithme.member.service.request.UpdateServiceRequest;
import team4.footwithme.member.service.response.LoginResponse;
import team4.footwithme.member.service.response.MemberResponse;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final SecurityConfig jwtSecurityConfig;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MemberResponse join(JoinServiceRequest serviceRequest) {
        if (memberRepository.existByEmail(serviceRequest.email()))
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");

        Member member = serviceRequest.toEntity();
        member.encodePassword(jwtSecurityConfig.passwordEncoder());
        memberRepository.save(member);

        return MemberResponse.from(member);

    }

    @Override
    @Transactional
    public LoginResponse login(LoginServiceRequest serviceRequest) {
        Member member = memberRepository.findByEmail(serviceRequest.email())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        if (!jwtSecurityConfig.passwordEncoder().matches(serviceRequest.password(), member.getPassword())) {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }

        TokenResponse tokenResponse = jwtTokenUtil.createToken(member.getEmail());
        redisTemplate.opsForValue().set(member.getEmail(), tokenResponse.refreshToken(), tokenResponse.refreshTokenExpirationTime(), TimeUnit.MICROSECONDS);
        // Redis에 RefreshToken 저장

        return LoginResponse.from(tokenResponse);
    }

    @Override
    @Transactional
    public String logout(HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);
        String email = jwtTokenUtil.getEmailFromToken(accessToken);

        jwtTokenUtil.tokenValidation(accessToken);

        if(redisTemplate.opsForValue().get(email) != null){
            redisTemplate.delete(email);
        }

        long expiration = jwtTokenUtil.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MICROSECONDS);

        return "Success Logout";
    }

    @Override
    public TokenResponse reissue(HttpServletRequest request, String refreshToken) {
        if(refreshToken == null || refreshToken.isEmpty()){
            refreshToken = JwtTokenFilter.getRefreshTokenByRequest(request); // 헤더에 없을 경우 쿠키에서 꺼내 씀
        }

        jwtTokenUtil.tokenValidation(refreshToken);

        String newAccessToken = jwtTokenUtil.reCreateAccessToken(refreshToken);
        long refreshTokenExpirationTime = jwtTokenUtil.getExpiration(refreshToken);

        return TokenResponse.of(newAccessToken,
                refreshToken,
                refreshTokenExpirationTime);
    }

    @Override
    @Transactional
    public MemberResponse update(PrincipalDetails principalDetails, UpdateServiceRequest request) {
        Member member = principalDetails.getMember();
        member.update(request.name(), request.phoneNumber(), request.gender());
        memberRepository.save(member);

        return MemberResponse.from(member);
    }

    @Override
    @Transactional
    public String updatePassword(PrincipalDetails principalDetails, UpdatePasswordServiceRequest serviceRequest) {
        Member member = principalDetails.getMember();

        if(!jwtSecurityConfig.passwordEncoder().matches(serviceRequest.prePassword(), member.getPassword())) {
            throw new IllegalArgumentException("이전 패스워드가 일치하지 않습니다.");
        }
        member.changePassword(passwordEncoder.encode(serviceRequest.newPassword()));
        memberRepository.save(member);

        return "Success Change Password";
    }

}
