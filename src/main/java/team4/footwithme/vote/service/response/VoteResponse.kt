package team4.footwithme.vote.service.response

import team4.footwithme.vote.domain.Vote
import java.time.LocalDateTime

@JvmRecord
data class VoteResponse(
    val voteId: Long?,
    val title: String?,
    val endAt: LocalDateTime?,
    val voteStatus: String?,
    val choices: List<VoteItemResponse>
) {

    companion object {
        fun of(vote: Vote, choices: List<VoteItemResponse>): VoteResponse {
            return VoteResponse(
                vote.voteId,
                vote.title,
                vote.endAt,
                vote.voteStatus!!.text,
                choices
            )
        }
    }
}
