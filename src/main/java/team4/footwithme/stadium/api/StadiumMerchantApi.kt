package team4.footwithme.stadium.api

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.member.jwt.PrincipalDetails
import team4.footwithme.stadium.api.request.StadiumRegisterRequest
import team4.footwithme.stadium.api.request.StadiumUpdateRequest
import team4.footwithme.stadium.service.StadiumService
import team4.footwithme.stadium.service.response.StadiumDetailResponse

@RestController
@RequestMapping("/api/v1/merchant/stadium")
class StadiumMerchantApi(private val stadiumService: StadiumService) {
    //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    @PostMapping("/register")
    fun registerStadium(
        @Validated @RequestBody request: StadiumRegisterRequest,
        @AuthenticationPrincipal currentUser: PrincipalDetails
    ): ApiResponse<StadiumDetailResponse?> {
        return ApiResponse.created(
            stadiumService.registerStadium(
                request.toServiceRequest(),
                currentUser.member
            )
        )
    }

    //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    @PutMapping("/{stadiumId}")
    fun updateStadium(
        @PathVariable stadiumId: Long?,
        @Validated @RequestBody request: StadiumUpdateRequest,
        @AuthenticationPrincipal currentUser: PrincipalDetails
    ): ApiResponse<StadiumDetailResponse?> {
        return ApiResponse.ok(
            stadiumService.updateStadium(
                request.toServiceRequest(),
                currentUser.member,
                stadiumId
            )
        )
    }

    //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    @DeleteMapping("/{stadiumId}")
    fun deleteStadium(
        @PathVariable stadiumId: Long?,
        @AuthenticationPrincipal currentUser: PrincipalDetails
    ): ApiResponse<Void?> {
        stadiumService.deleteStadium(currentUser.member, stadiumId)
        return ApiResponse.ok(null)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(StadiumMerchantApi::class.java)
    }
}