package team4.footwithme.chat.service.event

@JvmRecord
data class TeamPublishedEvent(
    val name: String?,
    val teamId: Long?
)
