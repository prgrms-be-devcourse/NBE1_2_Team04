package team4.footwithme.member.api;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.api.request.JoinRequest;
import team4.footwithme.member.api.request.LoginRequest;
import team4.footwithme.member.service.MemberService;
import team4.footwithme.member.service.response.LoginResponse;
import team4.footwithme.member.service.response.MemberResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberApi {

    private final MemberService memberService;

    @PostMapping("/join")
    public ApiResponse<MemberResponse> join(@RequestBody @Valid JoinRequest request){
        return ApiResponse.created(memberService.join(request.toServiceRequest()));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response){
        return ApiResponse.ok(memberService.login(request.toServiceRequest(), response));
    }

}
