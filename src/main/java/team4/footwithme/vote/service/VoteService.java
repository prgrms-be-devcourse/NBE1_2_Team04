package team4.footwithme.vote.service;

import team4.footwithme.vote.service.request.VoteCreateServiceRequest;
import team4.footwithme.vote.service.response.VoteResponse;

public interface VoteService {
    VoteResponse createStadiumVote(VoteCreateServiceRequest request, Long teamId, String email);
}
