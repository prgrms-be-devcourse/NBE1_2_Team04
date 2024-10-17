package team4.footwithme.team.api.request

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import team4.footwithme.team.service.request.TeamDefaultServiceRequest

@JvmRecord
data class TeamCreateRequest(
    val name: @NotNull(message = "팀 명은 필수입니다.") String?,
    val description: @Null String?,
    val location: @Null String?
) {
    fun toServiceRequest(): TeamDefaultServiceRequest {
        return TeamDefaultServiceRequest(
            name, description, location
        )
    }
}
