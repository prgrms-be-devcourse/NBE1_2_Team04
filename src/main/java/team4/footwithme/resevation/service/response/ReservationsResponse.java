package team4.footwithme.resevation.service.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import team4.footwithme.resevation.domain.ParticipantGender;
import team4.footwithme.resevation.domain.Reservation;

import java.time.LocalDateTime;

public record ReservationsResponse(
        Long reservationId,
        Long courtId,
        Long memberId,
        Long teamId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime matchDate,
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
