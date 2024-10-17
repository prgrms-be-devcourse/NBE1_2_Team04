package team4.footwithme.resevation.service

import org.springframework.data.domain.Slice
import team4.footwithme.member.domain.Member
import team4.footwithme.resevation.service.response.ReservationInfoDetailsResponse
import team4.footwithme.resevation.service.response.ReservationInfoResponse
import team4.footwithme.resevation.service.response.ReservationsResponse
import java.time.LocalDateTime

interface ReservationService {
    fun findReadyReservations(reservationId: Long?, page: Int?): Slice<ReservationsResponse>

    fun createReservation(
        memberId: Long?,
        courtId: Long?,
        teamId: Long,
        matchDate: LocalDateTime?,
        memberIds: List<Long?>
    )

    fun getTeamReservationInfo(teamId: Long?): List<ReservationInfoResponse>

    fun getTeamReservationInfoDetails(reservationId: Long): ReservationInfoDetailsResponse

    fun deleteReservation(reservationId: Long, member: Member?): Long
}