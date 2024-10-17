package team4.footwithme.resevation.service.response

import com.fasterxml.jackson.annotation.JsonFormat
import team4.footwithme.resevation.domain.ParticipantGender
import team4.footwithme.resevation.domain.Reservation
import java.time.LocalDateTime

@JvmRecord
data class ReservationsResponse(
    val reservationId: Long?,
    val courtId: Long?,
    val memberId: Long?,
    val teamId: Long?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val matchDate: LocalDateTime?,
    val gender: ParticipantGender?
) {

    companion object {
        fun from(reservation: Reservation?): ReservationsResponse {
            return ReservationsResponse(
                reservation!!.reservationId,
                reservation.court!!.courtId,
                reservation.member!!.memberId,
                reservation.team!!.teamId,
                reservation.matchDate,
                reservation.gender
            )
        }
    }
}
