package team4.footwithme.vote.service;

import team4.footwithme.member.domain.Member;
import team4.footwithme.vote.service.request.ChoiceCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteDateCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteStadiumCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteUpdateServiceRequest;
import team4.footwithme.vote.service.response.VoteResponse;

public interface VoteService {

    VoteResponse createStadiumVote(VoteStadiumCreateServiceRequest request, Long teamId, Member member);

    VoteResponse getStadiumVote(Long voteId);

    VoteResponse createDateVote(VoteDateCreateServiceRequest request, Long teamId, Member member);

    VoteResponse getDateVote(Long voteId);

    Long deleteVote(Long voteId, Member member);

    VoteResponse createChoice(ChoiceCreateServiceRequest request, Long voteId, Member member);

    VoteResponse deleteChoice(Long voteId, Member member);

    VoteResponse updateVote(VoteUpdateServiceRequest serviceRequest, Long voteId, Member member);

    VoteResponse closeVote(Long voteId, Member member);
}
