package team4.footwithme.vote.service.request

import java.time.LocalDateTime

@JvmRecord
data class VoteUpdateServiceRequest(
    val title: String?,
    val endAt: LocalDateTime?
) {
}
