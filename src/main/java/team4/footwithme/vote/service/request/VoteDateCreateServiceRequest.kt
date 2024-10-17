package team4.footwithme.vote.service.request

import java.time.LocalDateTime

@JvmRecord
data class VoteDateCreateServiceRequest(
    val title: String?,
    val endAt: LocalDateTime?,
    val choices: List<LocalDateTime>
) {
}
