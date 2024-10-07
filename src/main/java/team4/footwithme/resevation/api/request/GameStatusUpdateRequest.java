package team4.footwithme.resevation.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import team4.footwithme.resevation.domain.GameStatus;
import team4.footwithme.resevation.service.request.GameStatusUpdateServiceRequest;

public record GameStatusUpdateRequest(
        @NotNull(message = "게임 ID는 필수입니다.")
        Long gameId,
        @NotNull(message = "상태는 필수입니다.")
        @Pattern(regexp = "READY|IGNORE", message = "상태는 READY 또는 IGNORE만 가능합니다.")
        GameStatus status
) {
    public GameStatusUpdateServiceRequest toServiceRequest() {
        return new GameStatusUpdateServiceRequest(gameId, status);
    }
}
