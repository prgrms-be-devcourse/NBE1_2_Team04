package team4.footwithme.resevation.api.request

import jakarta.validation.constraints.NotNull
import team4.footwithme.resevation.service.request.MercenaryServiceRequest

@JvmRecord
data class MercenaryRequest(
    val reservationId: @NotNull Long?,
    val description: String
) {
    fun toServiceRequest(): MercenaryServiceRequest {
        return MercenaryServiceRequest(reservationId, description)
    }
}
