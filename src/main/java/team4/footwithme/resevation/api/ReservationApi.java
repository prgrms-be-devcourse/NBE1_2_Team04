package team4.footwithme.resevation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.resevation.service.ReservationService;
import team4.footwithme.resevation.service.response.ReservationsResponse;

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
}
