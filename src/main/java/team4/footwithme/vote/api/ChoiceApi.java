package team4.footwithme.vote.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.vote.api.request.ChoiceCreateRequest;
import team4.footwithme.vote.service.VoteService;
import team4.footwithme.vote.service.response.VoteResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/choice")
public class ChoiceApi {

    private final VoteService voteService;

    @PostMapping("/{voteId}")
    public ApiResponse<VoteResponse> createChoice(@Valid @RequestBody ChoiceCreateRequest request, @PathVariable Long voteId) {
        String email = "test@gmail.com";
        return ApiResponse.created(voteService.createChoice(request.toServiceRequest(), voteId, email));
    }

}
