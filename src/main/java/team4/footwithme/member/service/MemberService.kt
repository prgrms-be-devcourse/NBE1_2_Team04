package team4.footwithme.member.service

import jakarta.servlet.http.HttpServletRequest
import team4.footwithme.member.domain.Member
import team4.footwithme.member.jwt.response.TokenResponse
import team4.footwithme.member.service.request.JoinServiceRequest
import team4.footwithme.member.service.request.LoginServiceRequest
import team4.footwithme.member.service.request.UpdatePasswordServiceRequest
import team4.footwithme.member.service.request.UpdateServiceRequest
import team4.footwithme.member.service.response.LoginResponse
import team4.footwithme.member.service.response.MemberResponse

interface MemberService {
    fun join(serviceRequest: JoinServiceRequest?): MemberResponse

    fun login(serviceRequest: LoginServiceRequest?): LoginResponse

    fun logout(request: HttpServletRequest): String

    fun reissue(request: HttpServletRequest, refreshToken: String?): TokenResponse

    fun update(member: Member?, request: UpdateServiceRequest?): MemberResponse

    fun updatePassword(member: Member?, serviceRequest: UpdatePasswordServiceRequest?): String
}
