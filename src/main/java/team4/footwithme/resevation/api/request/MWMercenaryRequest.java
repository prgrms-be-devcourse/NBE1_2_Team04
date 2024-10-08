package team4.footwithme.resevation.api.request;

import jakarta.validation.constraints.NotNull;
import team4.footwithme.resevation.service.request.MWMercenaryServiceRequest;

public record MWMercenaryRequest(
        @NotNull
        Long reservationId,
        String description) {
    public MWMercenaryServiceRequest toServiceRequest() {
        return new MWMercenaryServiceRequest(reservationId, description);
    }
}
