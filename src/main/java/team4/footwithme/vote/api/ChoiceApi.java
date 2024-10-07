package team4.footwithme.vote.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.vote.api.request.ChoiceCreateRequest;
import team4.footwithme.vote.service.VoteService;
import team4.footwithme.vote.service.response.VoteResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/choice")
public class ChoiceApi {

    private final VoteService voteService;

    @PostMapping("/{voteId}")
    public ApiResponse<VoteResponse> createChoice(@Valid @RequestBody ChoiceCreateRequest request, @PathVariable Long voteId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.created(voteService.createChoice(request.toServiceRequest(), voteId, principalDetails.getMember()));
    }

    @DeleteMapping("/{voteId}")
    public ApiResponse<VoteResponse> deleteChoice(@PathVariable Long voteId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.ok(voteService.deleteChoice(voteId, principalDetails.getMember()));
    }

}
