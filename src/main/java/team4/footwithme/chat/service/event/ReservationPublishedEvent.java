package team4.footwithme.chat.service.event;

public record ReservationPublishedEvent(
    String name,
    Long reservationId
) {
}
