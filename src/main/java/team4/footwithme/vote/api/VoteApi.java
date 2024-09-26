package team4.footwithme.vote.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.vote.api.request.VoteStadiumCreateRequest;
import team4.footwithme.vote.service.VoteService;
import team4.footwithme.vote.service.response.VoteResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/votes")
public class VoteApi {

    private final VoteService voteService;

    @PostMapping("/stadiums/{teamId}")
    public ApiResponse<VoteResponse> createLocateVote(@Valid @RequestBody VoteStadiumCreateRequest request, @PathVariable Long teamId) {
        String email = "email@test.com";
        return ApiResponse.created(voteService.createStadiumVote(request.toServiceRequest(),teamId, email));
    }

}
