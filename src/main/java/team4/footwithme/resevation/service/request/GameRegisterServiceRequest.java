package team4.footwithme.resevation.service.request;

public record GameRegisterServiceRequest(
        Long firstReservationId,
        Long secondReservationId
) {
}
