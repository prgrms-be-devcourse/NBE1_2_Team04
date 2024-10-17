package team4.footwithme.vote.api

import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.member.jwt.PrincipalDetails
import team4.footwithme.vote.api.request.ChoiceCreateRequest
import team4.footwithme.vote.service.VoteService
import team4.footwithme.vote.service.response.VoteResponse

@RestController
@RequestMapping("/api/v1/choice")
class ChoiceApi(private val voteService: VoteService) {
    @PostMapping("/{voteId}")
    fun createChoice(
        @RequestBody request: @Valid ChoiceCreateRequest?,
        @PathVariable voteId: Long?,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<VoteResponse?> {
        return ApiResponse.created(
            voteService.createChoice(
                request!!.toServiceRequest(),
                voteId,
                principalDetails.member
            )
        )
    }

    @DeleteMapping("/{voteId}")
    fun deleteChoice(
        @PathVariable voteId: Long?,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<VoteResponse?> {
        return ApiResponse.ok(voteService.deleteChoice(voteId, principalDetails.member))
    }
}
