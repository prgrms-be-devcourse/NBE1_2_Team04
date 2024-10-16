package team4.footwithme.resevation.api.request;

import jakarta.validation.constraints.NotNull;
import team4.footwithme.resevation.domain.ReservationStatus;
import team4.footwithme.resevation.service.request.ReservationUpdateServiceRequest;

public record ReservationUpdateRequest(
        @NotNull
        Long reservationId,
        @NotNull
        ReservationStatus status
) {
    public ReservationUpdateServiceRequest toServiceRequest() {
        return new ReservationUpdateServiceRequest(reservationId, status);
    }
}
