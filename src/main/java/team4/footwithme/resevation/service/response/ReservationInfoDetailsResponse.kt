package team4.footwithme.resevation.service.response

import team4.footwithme.resevation.domain.*
import java.time.LocalDateTime
import java.util.function.Function
import java.util.stream.Collectors

// TODO :: ReservationInfoDetailsResponse 이름 변경 부탁
@JvmRecord
data class ReservationInfoDetailsResponse(
    val courtName: String?,
    val matchTeamName: String?,
    val matchDate: LocalDateTime?,
    val participants: List<ParticipantInfoResponse>,
    val gender: ParticipantGender?,
    val status: ReservationStatus?
) {

    companion object {
        fun of(
            reservation: Reservation,
            participants: List<Participant?>?,
            matchTeamName: String?
        ): ReservationInfoDetailsResponse {
            val participantResponses = participants!!.stream()
                .map<ParticipantInfoResponse>(Function<Participant?, ParticipantInfoResponse> { participant: Participant? ->
                    ParticipantInfoResponse.Companion.from(
                        participant
                    )
                })
                .collect(Collectors.toList<ParticipantInfoResponse>())

            return ReservationInfoDetailsResponse(
                reservation.court!!.name,
                matchTeamName,
                reservation.matchDate,
                participantResponses,
                reservation.gender,
                reservation.reservationStatus
            )
        }
    }
}
