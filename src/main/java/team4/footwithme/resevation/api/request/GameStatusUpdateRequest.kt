package team4.footwithme.resevation.api.request

import jakarta.validation.constraints.NotNull
import team4.footwithme.resevation.domain.GameStatus
import team4.footwithme.resevation.service.request.GameStatusUpdateServiceRequest

@JvmRecord
data class GameStatusUpdateRequest(
    val gameId: @NotNull(message = "게임 ID는 필수입니다.") Long?,
    val status: @NotNull(message = "상태는 필수입니다.") GameStatus?
) {
    fun toServiceRequest(): GameStatusUpdateServiceRequest {
        return GameStatusUpdateServiceRequest(gameId, status)
    }
}
