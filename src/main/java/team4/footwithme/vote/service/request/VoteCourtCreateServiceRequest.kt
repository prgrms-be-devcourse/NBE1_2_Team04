package team4.footwithme.vote.service.request

import java.time.LocalDateTime

@JvmRecord
data class VoteCourtCreateServiceRequest(
    val title: String?,
    val endAt: LocalDateTime?,
    val courtIds: List<Long?>
) {
}
