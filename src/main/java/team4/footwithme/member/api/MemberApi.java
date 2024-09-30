package team4.footwithme.member.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.api.request.JoinRequest;
import team4.footwithme.member.api.request.LoginRequest;
import team4.footwithme.member.jwt.JwtTokenUtil;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.member.service.CookieService;
import team4.footwithme.member.service.MemberService;
import team4.footwithme.member.service.response.LoginResponse;
import team4.footwithme.member.service.response.MemberResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberApi {

    private final MemberService memberService;
    private final CookieService cookieService;

    @PostMapping("/join")
    public ApiResponse<MemberResponse> join(@RequestBody @Valid JoinRequest request){
        return ApiResponse.created(memberService.join(request.toServiceRequest()));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response){
        LoginResponse loginResponse = memberService.login(request.toServiceRequest());
        cookieService.setHeader(response, loginResponse.refreshToken());   // 쿠키에 refreshToken 저장

        return ApiResponse.ok(loginResponse);
    }



}
