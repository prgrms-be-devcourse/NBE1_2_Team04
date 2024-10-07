package team4.footwithme.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team4.footwithme.member.api.request.UpdateRequest;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.member.jwt.response.TokenResponse;
import team4.footwithme.member.service.request.JoinServiceRequest;
import team4.footwithme.member.service.request.LoginServiceRequest;
import team4.footwithme.member.service.request.UpdatePasswordServiceRequest;
import team4.footwithme.member.service.request.UpdateServiceRequest;
import team4.footwithme.member.service.response.LoginResponse;
import team4.footwithme.member.service.response.MemberResponse;

public interface MemberService {
    MemberResponse join(JoinServiceRequest serviceRequest);

    LoginResponse login(LoginServiceRequest serviceRequest);

    String logout(HttpServletRequest request);

    TokenResponse reissue(HttpServletRequest request, String refreshToken);

    MemberResponse update(Member member, UpdateServiceRequest request);

    String updatePassword(Member member, UpdatePasswordServiceRequest serviceRequest);
}
