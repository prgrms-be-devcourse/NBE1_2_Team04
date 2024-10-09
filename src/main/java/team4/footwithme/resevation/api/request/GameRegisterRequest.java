package team4.footwithme.resevation.api.request;

import jakarta.validation.constraints.NotNull;
import team4.footwithme.resevation.service.request.GameRegisterServiceRequest;

public record GameRegisterRequest(
        @NotNull(message = "신청 예약 아이디는 필수입니다.")
        Long firstReservationId,
        @NotNull(message = "신청 받는 예약 아이디는 필수입니다.")
        Long secondReservationId
) {
    public GameRegisterServiceRequest toServiceRequest(){
        return new GameRegisterServiceRequest(firstReservationId,secondReservationId);
    }
}
