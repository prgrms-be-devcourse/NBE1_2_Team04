package team4.footwithme.resevation.service.response;

import team4.footwithme.resevation.domain.ParticipantGender;
import team4.footwithme.resevation.domain.Reservation;

import java.time.LocalDateTime;

public record ReservationsResponse(
        Long reservationId,
        Long courtId,
        Long memberId,
        Long teamId,
        LocalDateTime matchDate,
        ParticipantGender gender
        ) {
    public static ReservationsResponse from(Reservation reservation) {
        return new ReservationsResponse(
                reservation.getReservationId(),
                reservation.getCourt().getCourtId(),
                reservation.getMember().getMemberId(),
                reservation.getTeam().getTeamId(),
                reservation.getMatchDate(),
                reservation.getGender()
        );
    }
}
