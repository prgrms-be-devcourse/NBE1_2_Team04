package team4.footwithme.resevation.service.response

import team4.footwithme.resevation.domain.Reservation
import team4.footwithme.resevation.domain.ReservationStatus
import java.time.LocalDateTime

// TODO :: ReservationInfoResponse 이름 변경 부탁
@JvmRecord
data class ReservationInfoResponse(
    val courtName: String?,
    val matchDate: LocalDateTime?,
    val status: ReservationStatus?
) {

    companion object {
        fun from(reservation: Reservation?): ReservationInfoResponse {
            return ReservationInfoResponse(
                reservation!!.court!!.name,
                reservation.matchDate,
                reservation.reservationStatus
            )
        }
    }
}
