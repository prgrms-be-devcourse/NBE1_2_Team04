package team4.footwithme.member.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate redisTemplate;

    private static final String COOKIE_REFRESH_TOKEN = "refreshToken";
    public static final String ACCESS_TOKEN = "Authorization";
    public static final String REFRESH_TOKEN = "refresh_token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String cookie_refreshToken = getRefreshTokenByRequest(request);
        String accessToken = jwtTokenUtil.getHeaderToken(request, ACCESS_TOKEN);
        String refreshToken = jwtTokenUtil.getHeaderToken(request, REFRESH_TOKEN);
        if(cookie_refreshToken != null){
            processSecurity(accessToken ,cookie_refreshToken, response);
        }

        if(cookie_refreshToken == null){
            processSecurity(accessToken, refreshToken, response);
        }

        filterChain.doFilter(request, response);
    }

    private void processSecurity(String accessToken, String refreshToken, HttpServletResponse response) throws ServletException {

        if (accessToken != null) {
            jwtTokenUtil.tokenValidation(accessToken);

            String isLogout = (String) redisTemplate.opsForValue().get(accessToken);

            if (ObjectUtils.isEmpty(isLogout)) {
                setAuthentication(jwtTokenUtil.getEmailFromToken(accessToken));
            }
        }
        if (accessToken == null && refreshToken != null) {
            jwtTokenUtil.refreshTokenValidation(refreshToken);
            setAuthentication(jwtTokenUtil.getEmailFromToken(refreshToken));
        }

    }

    public void setAuthentication(String email) {
        Authentication authentication = jwtTokenUtil.createAuthentication(email);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static String getRefreshTokenByRequest(HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();

        if(cookies != null && cookies.length != 0){
            return Arrays.stream(cookies)
                    .filter(c -> c.getName().equals(COOKIE_REFRESH_TOKEN)).findFirst().map(Cookie::getValue)
                    .orElse(null);
        }

        return null;
    }


}
