package team4.footwithme.resevation.api

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.member.jwt.PrincipalDetails
import team4.footwithme.resevation.api.request.MercenaryRequest
import team4.footwithme.resevation.service.MercenaryService
import team4.footwithme.resevation.service.response.MercenaryResponse

@RestController
@RequestMapping("/api/v1/mercenary")
class MercenaryApi(private val mercenaryService: MercenaryService) {
    @PostMapping
    fun createMercenary(
        @RequestBody request: @Valid MercenaryRequest?,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<MercenaryResponse?> {
        return ApiResponse.Companion.created<MercenaryResponse?>(
            mercenaryService.createMercenary(
                request!!.toServiceRequest(),
                principalDetails.member
            )
        )
    }

    @GetMapping("/{mercenaryId}")
    fun getMercenary(@PathVariable mercenaryId: Long?): ApiResponse<MercenaryResponse?> {
        return ApiResponse.Companion.ok<MercenaryResponse?>(mercenaryService.getMercenary(mercenaryId))
    }

    @GetMapping
    fun getMercenaries(@RequestParam page: Int, @RequestParam size: Int): ApiResponse<Page<MercenaryResponse>?> {
        val pageRequest = PageRequest.of(page - 1, size)
        return ApiResponse.Companion.ok<Page<MercenaryResponse>?>(mercenaryService.getMercenaries(pageRequest))
    }

    @DeleteMapping("/{mercenaryId}")
    fun deleteMercenary(
        @PathVariable mercenaryId: Long?,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<Long?> {
        return ApiResponse.Companion.ok<Long?>(mercenaryService.deleteMercenary(mercenaryId, principalDetails.member))
    }
}
