package team4.footwithme.resevation.service.response;

import team4.footwithme.resevation.domain.Game;
import team4.footwithme.resevation.domain.GameStatus;

public record GameDetailResponse(
        Long gameId,
        Long firstReservationId,
        Long secondReservationId,
        GameStatus gameStatus
) {
    public static GameDetailResponse from(Game game) {
        return new GameDetailResponse(
                game.getGameId(),
                game.getFirstTeamReservation().getReservationId(),
                game.getSecondTeamReservation().getReservationId(),
                game.getGameStatus()
        );
    }
}
