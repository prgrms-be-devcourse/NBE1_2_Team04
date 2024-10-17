package team4.footwithme.team.api.request

import jakarta.validation.constraints.Null
import team4.footwithme.team.service.request.TeamDefaultServiceRequest

@JvmRecord
data class TeamUpdateRequest(
    val name: @Null String?,
    val description: @Null String?,
    val location: @Null String?
) {
    fun toServiceRequest(): TeamDefaultServiceRequest {
        return TeamDefaultServiceRequest(
            name, description, location
        )
    }
}
