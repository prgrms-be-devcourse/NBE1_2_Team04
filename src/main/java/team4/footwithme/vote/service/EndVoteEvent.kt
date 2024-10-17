package team4.footwithme.vote.service

import java.time.LocalDateTime

@JvmRecord
data class EndVoteEvent(
    val courtId: Long?,
    val memberId: Long?,
    val teamId: Long?,
    val matchDate: LocalDateTime?,
    val memberIds: List<Long?>?
) {
}
