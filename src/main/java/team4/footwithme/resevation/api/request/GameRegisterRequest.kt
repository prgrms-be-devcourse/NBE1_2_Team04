package team4.footwithme.resevation.api.request

import jakarta.validation.constraints.NotNull
import team4.footwithme.resevation.service.request.GameRegisterServiceRequest

@JvmRecord
data class GameRegisterRequest(
    val firstReservationId: @NotNull(message = "신청 예약 아이디는 필수입니다.") Long?,
    val secondReservationId: @NotNull(message = "신청 받는 예약 아이디는 필수입니다.") Long?
) {
    fun toServiceRequest(): GameRegisterServiceRequest {
        return GameRegisterServiceRequest(firstReservationId, secondReservationId)
    }
}
