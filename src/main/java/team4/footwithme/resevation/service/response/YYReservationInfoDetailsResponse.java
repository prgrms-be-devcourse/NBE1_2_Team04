package team4.footwithme.resevation.service.response;

import team4.footwithme.resevation.domain.Participant;
import team4.footwithme.resevation.domain.ParticipantGender;
import team4.footwithme.resevation.domain.Reservation;
import team4.footwithme.resevation.domain.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record YYReservationInfoDetailsResponse(
        String courtName,
        String matchTeamName,
        LocalDateTime matchDate,
        List<YYParticipantResponse> participants,
        ParticipantGender gender,
        ReservationStatus status
) {
    public static YYReservationInfoDetailsResponse of(Reservation reservation, List<Participant> participants, String matchTeamName) {

        List<YYParticipantResponse> participantResponses = participants.stream()
                .map(YYParticipantResponse::from)
                .collect(Collectors.toList());

        return new YYReservationInfoDetailsResponse(
                reservation.getCourt().getName(),
                matchTeamName,
                reservation.getMatchDate(),
                participantResponses,
                reservation.getGender(),
                reservation.getReservationStatus()
        );
    }
}
