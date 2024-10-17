package team4.footwithme.resevation.api

import org.springframework.data.domain.Slice
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.member.jwt.PrincipalDetails
import team4.footwithme.resevation.service.ReservationService
import team4.footwithme.resevation.service.response.ReservationInfoDetailsResponse
import team4.footwithme.resevation.service.response.ReservationInfoResponse
import team4.footwithme.resevation.service.response.ReservationsResponse

@RestController
@RequestMapping("/api/v1/reservation")
class ReservationApi(private val reservationService: ReservationService) {
    @GetMapping("/ready")
    fun getReadyReservations(
        @RequestParam reservationId: Long?,
        @RequestParam(defaultValue = "0", required = false) page: Int?
    ): ApiResponse<Slice<ReservationsResponse>?> {
        return ApiResponse.Companion.ok<Slice<ReservationsResponse>?>(
            reservationService.findReadyReservations(
                reservationId,
                page
            )
        )
    }

    /**
     * 팀 경기 일정 조회
     */
    @GetMapping("/{teamId}")
    fun getTeamReservationInfo(@PathVariable teamId: Long?): ApiResponse<List<ReservationInfoResponse>?> {
        return ApiResponse.Companion.ok<List<ReservationInfoResponse>?>(reservationService.getTeamReservationInfo(teamId))
    }

    /**
     * 팀 경기 일정 상세 조회
     */
    @GetMapping("/details/{reservationId}")
    fun getTeamReservationInfoDetails(@PathVariable reservationId: Long): ApiResponse<ReservationInfoDetailsResponse?> {
        return ApiResponse.Companion.ok<ReservationInfoDetailsResponse?>(
            reservationService.getTeamReservationInfoDetails(
                reservationId
            )
        )
    }

    @DeleteMapping("/{reservationId}")
    fun deleteReservation(
        @PathVariable(value = "reservationId") reservationId: Long,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<Long?> {
        return ApiResponse.Companion.ok<Long?>(
            reservationService.deleteReservation(
                reservationId,
                principalDetails.member
            )
        )
    }
}
