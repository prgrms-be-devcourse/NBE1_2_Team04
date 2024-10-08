package team4.footwithme.vote.service;

import team4.footwithme.member.domain.Member;
import team4.footwithme.vote.service.request.ChoiceCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteDateCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteCourtCreateServiceRequest;
import team4.footwithme.vote.service.request.VoteUpdateServiceRequest;
import team4.footwithme.vote.service.response.AllVoteResponse;
import team4.footwithme.vote.service.response.VoteResponse;

import java.util.List;

public interface VoteService {

    VoteResponse createCourtVote(VoteCourtCreateServiceRequest request, Long teamId, Member member);

    VoteResponse createDateVote(VoteDateCreateServiceRequest request, Long teamId, Member member);

    VoteResponse getVote(Long voteId);

    Long deleteVote(Long voteId, Member member);

    VoteResponse createChoice(ChoiceCreateServiceRequest request, Long voteId, Member member);

    VoteResponse deleteChoice(Long voteId, Member member);

    VoteResponse updateVote(VoteUpdateServiceRequest serviceRequest, Long voteId, Member member);

    VoteResponse closeVote(Long voteId, Member member);

    List<AllVoteResponse> getAllVotes(Long teamId);
}
