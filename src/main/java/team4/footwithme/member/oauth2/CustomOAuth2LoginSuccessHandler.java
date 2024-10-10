package team4.footwithme.member.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.domain.MemberRole;
import team4.footwithme.member.jwt.JwtTokenUtil;
import team4.footwithme.member.jwt.response.TokenResponse;
import team4.footwithme.member.oauth2.response.MemberOAuthResponse;
import team4.footwithme.member.service.CookieService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate redisTemplate;
    private final CookieService cookieService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // OAuth2User로 캐스팅하여 인증된 사용자 정보를 가져온다.
        CustomOAuth2UserDetails oAuth2User = (CustomOAuth2UserDetails) authentication.getPrincipal();
        // 사용자를 가져온다.
        Member member = oAuth2User.getMember();

        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 최초 로그인인 경우
        if (member.getMemberRole() == MemberRole.GUEST) {
            ApiResponse<MemberOAuthResponse> apiResponse = ApiResponse.of(
                HttpStatus.OK,
                MemberOAuthResponse.from(member)
            );
            String jsonResponse = objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(jsonResponse);
            return;
        }
        // 회원 가입 기록이 있으면
        TokenResponse tokenResponse = jwtTokenUtil.createToken(member.getEmail());

        ApiResponse<TokenResponse> apiResponse = ApiResponse.of(
            HttpStatus.OK,
            tokenResponse
        );

        // Redis에 RefreshToken 저장
        redisTemplate.opsForValue().set(member.getEmail(), tokenResponse.refreshToken(), tokenResponse.refreshTokenExpirationTime(), TimeUnit.MICROSECONDS);
        cookieService.setHeader(response, tokenResponse.refreshToken()); // 쿠키에 refreshToken 저장
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(jsonResponse);
    }
}
