package team4.footwithme.resevation.service.request;

import team4.footwithme.resevation.domain.GameStatus;

public record GameStatusUpdateServiceRequest(
    Long gameId,
    GameStatus status
) {
}
