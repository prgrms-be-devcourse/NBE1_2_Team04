package team4.footwithme.vote.service.response

import team4.footwithme.vote.domain.Vote
import java.time.LocalDateTime

@JvmRecord
data class AllVoteResponse(
    val voteId: Long?,
    val title: String?,
    val endAt: LocalDateTime?,
    val status: String?
) {
    companion object {
        fun from(vote: Vote?): AllVoteResponse {
            return AllVoteResponse(
                vote!!.voteId,
                vote.title,
                vote.endAt,
                vote.voteStatus!!.text
            )
        }
    }
}
