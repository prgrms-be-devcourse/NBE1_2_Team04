package team4.footwithme.member.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import team4.footwithme.member.jwt.JwtTokenUtil;
import team4.footwithme.member.jwt.response.TokenResponse;

@Component
public class CookieService {
    public static ResponseCookie createRefreshToken(String refreshToken){
        return ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .maxAge(14 * 24 * 60 * 60 * 1000)
                .httpOnly(true)
                .build();
    }

    public void setHeader(HttpServletResponse response, String refreshToken){
        if(refreshToken != null){
            response.addHeader(JwtTokenUtil.REFRESH_TOKEN, refreshToken);
            response.addHeader("Set-Cookie", createRefreshToken(refreshToken).toString());
        }
    }

}
