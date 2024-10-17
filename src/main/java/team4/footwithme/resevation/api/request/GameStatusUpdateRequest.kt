package team4.footwithme.resevation.api.request;

import jakarta.validation.constraints.NotNull;
import team4.footwithme.resevation.domain.GameStatus;
import team4.footwithme.resevation.service.request.GameStatusUpdateServiceRequest;

public record GameStatusUpdateRequest(
    @NotNull(message = "게임 ID는 필수입니다.")
    Long gameId,
    @NotNull(message = "상태는 필수입니다.")
    GameStatus status
) {
    public GameStatusUpdateServiceRequest toServiceRequest() {
        return new GameStatusUpdateServiceRequest(gameId, status);
    }
}
