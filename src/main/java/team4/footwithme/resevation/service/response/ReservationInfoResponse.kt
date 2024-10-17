package team4.footwithme.resevation.service.response;

import team4.footwithme.resevation.domain.Reservation;
import team4.footwithme.resevation.domain.ReservationStatus;

import java.time.LocalDateTime;

// TODO :: ReservationInfoResponse 이름 변경 부탁
public record ReservationInfoResponse(
    String courtName,
    LocalDateTime matchDate,
    ReservationStatus status
) {
    public static ReservationInfoResponse from(Reservation reservation) {
        return new ReservationInfoResponse(
            reservation.getCourt().getName(),
            reservation.getMatchDate(),
            reservation.getReservationStatus()
        );
    }
}
