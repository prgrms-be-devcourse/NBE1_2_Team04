package team4.footwithme.resevation.api.request

import jakarta.validation.constraints.NotNull
import team4.footwithme.resevation.domain.ParticipantRole
import team4.footwithme.resevation.service.request.ParticipantUpdateServiceRequest

@JvmRecord
data class ParticipantUpdateRequest(
    val participantId: @NotNull Long?,
    val role: @NotNull ParticipantRole?
) {
    fun toServiceResponse(): ParticipantUpdateServiceRequest {
        return ParticipantUpdateServiceRequest(participantId, role)
    }
}
