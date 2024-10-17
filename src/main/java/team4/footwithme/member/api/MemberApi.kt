package team4.footwithme.member.api

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.member.api.request.JoinRequest
import team4.footwithme.member.api.request.LoginRequest
import team4.footwithme.member.api.request.UpdatePasswordRequest
import team4.footwithme.member.api.request.UpdateRequest
import team4.footwithme.member.jwt.JwtTokenUtil
import team4.footwithme.member.jwt.PrincipalDetails
import team4.footwithme.member.jwt.response.TokenResponse
import team4.footwithme.member.service.CookieService
import team4.footwithme.member.service.MemberService
import team4.footwithme.member.service.response.LoginResponse
import team4.footwithme.member.service.response.MemberResponse

@RestController
@RequestMapping("/api/v1/members")
class MemberApi(private val memberService: MemberService, private val cookieService: CookieService) {
    @PostMapping("/join")
    fun join(@RequestBody request: @Valid JoinRequest?): ApiResponse<MemberResponse?> {
        return ApiResponse.created(memberService.join(request!!.toServiceRequest()))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: @Valid LoginRequest?, response: HttpServletResponse): ApiResponse<LoginResponse?> {
        val loginResponse = memberService.login(request!!.toServiceRequest())
        cookieService.setHeader(response, loginResponse!!.refreshToken) // 쿠키에 refreshToken 저장

        return ApiResponse.ok(loginResponse)
    }

    @DeleteMapping("/logout")
    fun logout(request: HttpServletRequest): ApiResponse<String?> {
        return ApiResponse.ok(memberService.logout(request))
    }

    @PostMapping("/reissue")
    fun reissue(
        request: HttpServletRequest, response: HttpServletResponse,
        @RequestHeader(name = JwtTokenUtil.Companion.REFRESH_TOKEN, defaultValue = "") refreshToken: String?
    ): ApiResponse<TokenResponse?> {
        val tokenResponse = memberService.reissue(request, refreshToken)
        cookieService.setHeader(response, tokenResponse!!.refreshToken) // 쿠키에 refreshToken 저장

        return ApiResponse.ok(tokenResponse)
    }

    @PutMapping("/update")
    fun update(
        @AuthenticationPrincipal principalDetails: PrincipalDetails,
        @RequestBody request: @Valid UpdateRequest?
    ): ApiResponse<MemberResponse?> {
        return ApiResponse.ok(memberService.update(principalDetails.member, request!!.toServiceRequest()))
    }

    @PutMapping("/update-password")
    fun updatePassword(
        @AuthenticationPrincipal principalDetails: PrincipalDetails,
        @RequestBody request: @Valid UpdatePasswordRequest?
    ): ApiResponse<String?> {
        return ApiResponse.ok(memberService.updatePassword(principalDetails.member, request!!.toServiceRequest()))
    }
}
