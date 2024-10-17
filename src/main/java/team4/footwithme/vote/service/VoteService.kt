package team4.footwithme.vote.service

import team4.footwithme.member.domain.Member
import team4.footwithme.vote.domain.Vote
import team4.footwithme.vote.service.request.ChoiceCreateServiceRequest
import team4.footwithme.vote.service.request.VoteCourtCreateServiceRequest
import team4.footwithme.vote.service.request.VoteDateCreateServiceRequest
import team4.footwithme.vote.service.request.VoteUpdateServiceRequest
import team4.footwithme.vote.service.response.AllVoteResponse
import team4.footwithme.vote.service.response.VoteResponse

interface VoteService {
    fun createCourtVote(request: VoteCourtCreateServiceRequest?, teamId: Long, member: Member?): VoteResponse

    fun createDateVote(request: VoteDateCreateServiceRequest?, teamId: Long, member: Member?): VoteResponse

    fun getVote(voteId: Long?): VoteResponse

    fun deleteVote(voteId: Long?, member: Member?): Long?

    fun createChoice(request: ChoiceCreateServiceRequest?, voteId: Long?, member: Member?): VoteResponse

    fun deleteChoice(voteId: Long?, member: Member?): VoteResponse

    fun updateVote(serviceRequest: VoteUpdateServiceRequest?, voteId: Long?, member: Member?): VoteResponse

    fun closeVote(voteId: Long?, member: Member?): VoteResponse

    fun getAllVotes(teamId: Long): List<AllVoteResponse>?

    fun makeReservation(vote: Vote)
}
