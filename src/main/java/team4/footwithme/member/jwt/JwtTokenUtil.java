package team4.footwithme.member.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.domain.MemberRole;
import team4.footwithme.member.jwt.response.TokenResponse;
import team4.footwithme.member.repository.MemberRepository;

import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final PrincipalDetailsService userDetailService;
    private final MemberRepository memberRepository;
    private final RedisTemplate redisTemplate;


    public static final String ACCESS_TOKEN = "Authorization";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String BEARER_PREFIX = "Bearer ";

    public static final long ACCESS_TIME = Duration.ofMinutes(30).toMillis(); // 만료시간 30분
    public static final long REFRESH_TIME = Duration.ofDays(14).toMillis(); // 만료시간 2주

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String getHeaderToken(HttpServletRequest request, String type) {
        if(type.equals("Access"))
            return request.getHeader(ACCESS_TOKEN);

        return request.getHeader(REFRESH_TOKEN);
    }

    public TokenResponse createToken(String email){
        String accessToken = createAccessToken(email);
        String refreshToken = createRefreshToken(email);

        return TokenResponse.of(accessToken, refreshToken, REFRESH_TIME);
    }

    public String createAccessToken(String email) {
        Date date = new Date();
        MemberRole role = getRoleFromEmail(email);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.name())
                .setExpiration(new Date(date.getTime() + ACCESS_TIME))
                .setIssuedAt(date)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String reCreateAccessToken(String refreshToken){
        Date date = new Date();
        String email = getEmailFromToken(refreshToken);
        MemberRole role = getRoleFromEmail(email);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.name())
                .setExpiration(new Date(date.getTime() + ACCESS_TIME))
                .setIssuedAt(date)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String email){
        Date date = new Date();

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(date.getTime() + REFRESH_TIME))
                .setIssuedAt(date)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return refreshToken;
    }

    public Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();

        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    private MemberRole getRoleFromEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        return member.getMemberRole();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.hasText(token)) {
            if(token.startsWith(BEARER_PREFIX)) {
                return token.substring(7).trim();
                }
            return token;
            }

            return null;
        }

        public void tokenValidation(String token) {
            try {
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            } catch (SecurityException | MalformedJwtException e) {
                throw new JwtException("유효하지 않은 JWT 토큰입니다.");
            } catch (ExpiredJwtException e) {
                throw new JwtException ("만료된 JWT 입니다.");
            } catch (UnsupportedJwtException e) {
                throw new JwtException ("지원하지 않은 JWT 입니다.");
            } catch (IllegalArgumentException e) {
                throw new JwtException ("JWT 값이 비어있습니다.");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
    }

    public void refreshTokenValidation(String refreshToken){
        tokenValidation(refreshToken);
        String email = getEmailFromToken(refreshToken);
        String redisRefreshToken = null;

        Object redisRefresh = redisTemplate.opsForValue().get(email);

        if(redisRefresh != null)
            redisRefreshToken = redisRefresh.toString();

        if(redisRefreshToken == null)
            throw new JwtException("유효하지 않은 JWT 토큰입니다.");

    }

}
