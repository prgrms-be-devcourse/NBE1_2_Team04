package team4.footwithme.resevation.service.request

import team4.footwithme.resevation.domain.GameStatus

@JvmRecord
data class GameStatusUpdateServiceRequest(
    val gameId: Long?,
    val status: GameStatus?
) {
}
