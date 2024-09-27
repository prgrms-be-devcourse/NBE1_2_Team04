package team4.footwithme.vote.service;

import team4.footwithme.vote.service.request.VoteDateCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteStadiumCreateServiceRequest;
import team4.footwithme.vote.service.response.VoteResponse;

public interface VoteService {
    VoteResponse createStadiumVote(VoteStadiumCreateServiceRequest request, Long teamId, String email);

    VoteResponse getStadiumVote(long voteId);

    VoteResponse createDateVote(VoteDateCreateServiceRequest request, Long teamId, String email);
}
