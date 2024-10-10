package team4.footwithme.resevation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.resevation.service.ReservationService;
import team4.footwithme.resevation.service.response.ReservationInfoDetailsResponse;
import team4.footwithme.resevation.service.response.ReservationInfoResponse;
import team4.footwithme.resevation.service.response.ReservationsResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationApi {

    private final ReservationService reservationService;

    @GetMapping("/ready")
    public ApiResponse<Slice<ReservationsResponse>> getReadyReservations(
        @RequestParam Long reservationId,
        @RequestParam(defaultValue = "0", required = false) Integer page) {
        return ApiResponse.ok(reservationService.findReadyReservations(reservationId, page));
    }

    /**
     * 팀 경기 일정 조회
     */
    @GetMapping("/{teamId}")
    public ApiResponse<List<ReservationInfoResponse>> getTeamReservationInfo(@PathVariable Long teamId) {
        return ApiResponse.ok(reservationService.getTeamReservationInfo(teamId));
    }

    /**
     * 팀 경기 일정 상세 조회
     */
    @GetMapping("/details/{reservationId}")
    public ApiResponse<ReservationInfoDetailsResponse> getTeamReservationInfoDetails(@PathVariable Long reservationId) {
        return ApiResponse.ok(reservationService.getTeamReservationInfoDetails(reservationId));
    }

    @DeleteMapping("/{reservationId}")
    public ApiResponse<Long> deleteReservation(@PathVariable(value = "reservationId") Long reservationId,
                                               @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.ok(reservationService.deleteReservation(reservationId, principalDetails.getMember()));
    }
}
