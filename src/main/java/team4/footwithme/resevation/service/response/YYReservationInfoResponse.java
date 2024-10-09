package team4.footwithme.resevation.service.response;

import team4.footwithme.resevation.domain.Reservation;
import team4.footwithme.resevation.domain.ReservationStatus;

import java.time.LocalDateTime;

public record YYReservationInfoResponse(
        String courtName,
        LocalDateTime matchDate,
        ReservationStatus status
) {
    public static YYReservationInfoResponse from(Reservation reservation) {
        return new YYReservationInfoResponse(
                reservation.getCourt().getName(),
                reservation.getMatchDate(),
                reservation.getReservationStatus()
        );
    }
}
