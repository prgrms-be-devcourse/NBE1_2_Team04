package team4.footwithme.vote.api

import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.member.jwt.PrincipalDetails
import team4.footwithme.vote.api.request.VoteCourtCreateRequest
import team4.footwithme.vote.api.request.VoteDateCreateRequest
import team4.footwithme.vote.api.request.VoteUpdateRequest
import team4.footwithme.vote.service.VoteService
import team4.footwithme.vote.service.response.AllVoteResponse
import team4.footwithme.vote.service.response.VoteResponse

@RestController
@RequestMapping("/api/v1/votes")
class VoteApi(private val voteService: VoteService) {
    @PostMapping("/stadiums/{teamId}")
    fun createLocateVote(
        @RequestBody request: @Valid VoteCourtCreateRequest?,
        @PathVariable teamId: Long,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<VoteResponse?> {
        return ApiResponse.Companion.created<VoteResponse?>(
            voteService.createCourtVote(
                request!!.toServiceRequest(),
                teamId,
                principalDetails.member
            )
        )
    }

    @GetMapping("{voteId}")
    fun getVote(@PathVariable voteId: Long?): ApiResponse<VoteResponse?> {
        return ApiResponse.Companion.ok<VoteResponse?>(voteService.getVote(voteId))
    }

    @PostMapping("/dates/{teamId}")
    fun createDateVote(
        @RequestBody request: @Valid VoteDateCreateRequest?,
        @PathVariable teamId: Long,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<VoteResponse?> {
        return ApiResponse.Companion.created<VoteResponse?>(
            voteService.createDateVote(
                request!!.toServiceRequest(),
                teamId,
                principalDetails.member
            )
        )
    }

    @DeleteMapping("{voteId}")
    fun deleteVote(
        @PathVariable voteId: Long?,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<Long?> {
        return ApiResponse.Companion.ok<Long?>(voteService.deleteVote(voteId, principalDetails.member))
    }

    @PutMapping("{voteId}")
    fun updateVote(
        @RequestBody request: @Valid VoteUpdateRequest?,
        @PathVariable voteId: Long?,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<VoteResponse?> {
        return ApiResponse.Companion.ok<VoteResponse?>(
            voteService.updateVote(
                request!!.toServiceRequest(),
                voteId,
                principalDetails.member
            )
        )
    }

    @PostMapping("/close/{voteId}")
    fun closeVote(
        @PathVariable voteId: Long?,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<VoteResponse?> {
        return ApiResponse.Companion.ok<VoteResponse?>(voteService.closeVote(voteId, principalDetails.member))
    }

    @GetMapping("/all/{teamId}")
    fun getAllVotes(@PathVariable teamId: Long): ApiResponse<List<AllVoteResponse>?> {
        return ApiResponse.Companion.ok<List<AllVoteResponse>?>(voteService.getAllVotes(teamId))
    }
}
