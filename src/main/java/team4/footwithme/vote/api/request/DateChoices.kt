package team4.footwithme.vote.api.request

import java.time.LocalDateTime

@JvmRecord
data class DateChoices(
    val choice: LocalDateTime
)
