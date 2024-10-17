package team4.footwithme.team.api.request

import jakarta.validation.Valid
import team4.footwithme.team.service.request.TeamMemberServiceRequest

@JvmRecord
data class TeamMemberRequest(
    val emails: @Valid MutableList<String?>?
) {
    fun toServiceRequest(): TeamMemberServiceRequest {
        return TeamMemberServiceRequest(
            emails //List
        )
    }
}
