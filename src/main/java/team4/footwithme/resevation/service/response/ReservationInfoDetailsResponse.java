package team4.footwithme.resevation.service.response;

import team4.footwithme.resevation.domain.Participant;
import team4.footwithme.resevation.domain.ParticipantGender;
import team4.footwithme.resevation.domain.Reservation;
import team4.footwithme.resevation.domain.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// TODO :: ReservationInfoDetailsResponse 이름 변경 부탁
public record ReservationInfoDetailsResponse(
    String courtName,
    String matchTeamName,
    LocalDateTime matchDate,
    List<ParticipantInfoResponse> participants,
    ParticipantGender gender,
    ReservationStatus status
) {
    public static ReservationInfoDetailsResponse of(Reservation reservation, List<Participant> participants, String matchTeamName) {

        List<ParticipantInfoResponse> participantResponses = participants.stream()
            .map(ParticipantInfoResponse::from)
            .collect(Collectors.toList());

        return new ReservationInfoDetailsResponse(
            reservation.getCourt().getName(),
            matchTeamName,
            reservation.getMatchDate(),
            participantResponses,
            reservation.getGender(),
            reservation.getReservationStatus()
        );
    }
}
