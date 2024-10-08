package team4.footwithme.resevation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.resevation.service.YYReservationService;
import team4.footwithme.resevation.service.response.YYReservationInfoDetailsResponse;
import team4.footwithme.resevation.service.response.YYReservationInfoResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservation")
public class YYReservationApi {

    private final YYReservationService reservationService;
    /**
     * 팀 경기 일정 조회
     */
    @GetMapping("/{teamId}")
    public ApiResponse<List<YYReservationInfoResponse>> getTeamReservationInfo(@PathVariable Long teamId) {
        return ApiResponse.ok(reservationService.getTeamReservationInfo(teamId));
    }

    /**
     * 팀 경기 일정 상세 조회
     */
    @GetMapping("/details/{reservationId}")
    public ApiResponse<YYReservationInfoDetailsResponse> getTeamReservationInfoDetails(@PathVariable Long reservationId) {
        return ApiResponse.ok(reservationService.getTeamReservationInfoDetails(reservationId));
    }
}
