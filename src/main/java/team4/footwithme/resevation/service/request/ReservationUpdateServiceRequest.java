package team4.footwithme.resevation.service.request;

import team4.footwithme.resevation.domain.ReservationStatus;

public record ReservationUpdateServiceRequest(
        Long reservationId,
        ReservationStatus status
) {
}
