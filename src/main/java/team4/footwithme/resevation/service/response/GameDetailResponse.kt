package team4.footwithme.resevation.service.response

import team4.footwithme.resevation.domain.Game
import team4.footwithme.resevation.domain.GameStatus

@JvmRecord
data class GameDetailResponse(
    val gameId: Long?,
    val firstReservationId: Long?,
    val secondReservationId: Long?,
    val gameStatus: GameStatus?
) {

    companion object {
        fun from(game: Game?): GameDetailResponse {
            return GameDetailResponse(
                game?.gameId,
                game?.firstTeamReservation?.reservationId,
                game?.secondTeamReservation?.reservationId,
                game?.gameStatus
            )
        }
    }
}
