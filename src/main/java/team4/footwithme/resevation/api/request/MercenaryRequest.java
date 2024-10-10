package team4.footwithme.resevation.api.request;

import jakarta.validation.constraints.NotNull;
import team4.footwithme.resevation.service.request.MercenaryServiceRequest;

public record MercenaryRequest(
    @NotNull
    Long reservationId,
    String description) {
    public MercenaryServiceRequest toServiceRequest() {
        return new MercenaryServiceRequest(reservationId, description);
    }
}
