package team4.footwithme.member.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.api.request.JoinReq;
import team4.footwithme.member.service.CookieService;
import team4.footwithme.member.service.MemberService;
import team4.footwithme.member.service.response.MemberResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberApi {

    private final MemberService memberService;

    @PostMapping("/join")
    public ApiResponse<MemberResponse> join(@RequestBody @Valid JoinReq request){
        return ApiResponse.created(memberService.join(request.toServiceRequest()));
    }

}
