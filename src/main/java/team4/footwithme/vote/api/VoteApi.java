package team4.footwithme.vote.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.vote.api.request.VoteDateCreateRequest;
import team4.footwithme.vote.api.request.VoteStadiumCreateRequest;
import team4.footwithme.vote.api.request.VoteUpdateRequest;
import team4.footwithme.vote.service.VoteService;
import team4.footwithme.vote.service.response.VoteResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/votes")
public class VoteApi {

    private final VoteService voteService;

    @PostMapping("/stadiums/{teamId}")
    public ApiResponse<VoteResponse> createLocateVote(@Valid @RequestBody VoteStadiumCreateRequest request, @PathVariable Long teamId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.created(voteService.createStadiumVote(request.toServiceRequest(),teamId, principalDetails.getUsername()));
    }

    @GetMapping("{voteId}")
    public ApiResponse<VoteResponse> getVote(@PathVariable Long voteId) {
        return ApiResponse.ok(voteService.getStadiumVote(voteId));
    }

    @PostMapping("/dates/{teamId}")
    public ApiResponse<VoteResponse> createDateVote(@Valid @RequestBody VoteDateCreateRequest request, @PathVariable Long teamId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.created(voteService.createDateVote(request.toServiceRequest(), teamId, principalDetails.getUsername()));
    }

    @GetMapping("/dates/{voteId}")
    public ApiResponse<VoteResponse> getDateVote(@PathVariable Long voteId) {
        return ApiResponse.ok(voteService.getDateVote(voteId));
    }

    @DeleteMapping("{voteId}")
    public ApiResponse<Long> deleteVote(@PathVariable Long voteId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.ok(voteService.deleteVote(voteId,principalDetails.getUsername()));
    }

    @PutMapping("{voteId}")
    public ApiResponse<VoteResponse> updateVote(@Valid @RequestBody VoteUpdateRequest request, @PathVariable Long voteId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.ok(voteService.updateVote(request.toServiceRequest(), voteId, principalDetails.getUsername()));
    }

}
