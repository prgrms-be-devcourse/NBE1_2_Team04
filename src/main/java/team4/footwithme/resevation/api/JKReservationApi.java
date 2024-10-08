package team4.footwithme.resevation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.resevation.service.JKReservationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservation")
public class JKReservationApi {

    private final JKReservationService JKReservationService;

    @DeleteMapping("/{reservationId}")
    public ApiResponse<Long> deleteReservation (@PathVariable(value = "reservationId") Long reservationId,
                                                @AuthenticationPrincipal PrincipalDetails principalDetails){
        return ApiResponse.ok(JKReservationService.deleteReservation(reservationId, principalDetails.getMember()));
    }
}
