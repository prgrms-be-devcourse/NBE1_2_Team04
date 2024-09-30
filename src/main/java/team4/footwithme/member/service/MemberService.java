package team4.footwithme.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team4.footwithme.member.jwt.response.TokenResponse;
import team4.footwithme.member.service.request.JoinServiceRequest;
import team4.footwithme.member.service.request.LoginServiceRequest;
import team4.footwithme.member.service.response.LoginResponse;
import team4.footwithme.member.service.response.MemberResponse;

public interface MemberService {
    MemberResponse join(JoinServiceRequest serviceRequest);

    LoginResponse login(LoginServiceRequest serviceRequest);

    String logout(HttpServletRequest request);

    TokenResponse reissue(HttpServletRequest request, String refreshToken);
}
