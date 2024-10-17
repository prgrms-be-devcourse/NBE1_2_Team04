package team4.footwithme.stadium.api

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.member.jwt.PrincipalDetails
import team4.footwithme.stadium.api.request.CourtDeleteRequest
import team4.footwithme.stadium.api.request.CourtRegisterRequest
import team4.footwithme.stadium.api.request.CourtUpdateRequest
import team4.footwithme.stadium.service.CourtService
import team4.footwithme.stadium.service.response.CourtDetailResponse

@RestController
@RequestMapping("/api/v1/merchant/court")
class CourtMerchantApi(private val courtService: CourtService) {
    //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    @PostMapping("/register")
    fun registerCourt(
        @Validated @RequestBody request: CourtRegisterRequest,
        @AuthenticationPrincipal currentUser: PrincipalDetails
    ): ApiResponse<CourtDetailResponse?> {
        return ApiResponse.created(
            courtService.registerCourt(
                request.toServiceRequest(),
                currentUser.member
            )
        )
    }

    //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    @PutMapping("/{courtId}")
    fun updateCourt(
        @PathVariable courtId: Long?,
        @Validated @RequestBody request: CourtUpdateRequest,
        @AuthenticationPrincipal currentUser: PrincipalDetails
    ): ApiResponse<CourtDetailResponse?> {
        return ApiResponse.ok(
            courtService.updateCourt(
                request.toServiceRequest(),
                currentUser.member,
                courtId
            )
        )
    }

    //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    @DeleteMapping("/{courtId}")
    fun deleteCourt(
        @PathVariable courtId: Long?,
        @Validated @RequestBody request: CourtDeleteRequest,
        @AuthenticationPrincipal currentUser: PrincipalDetails
    ): ApiResponse<Void?> {
        courtService.deleteCourt(request.toServiceRequest(), currentUser.member, courtId)
        return ApiResponse.ok(null)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CourtMerchantApi::class.java)
    }
}
