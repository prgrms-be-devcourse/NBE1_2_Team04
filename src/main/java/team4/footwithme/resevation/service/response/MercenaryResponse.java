package team4.footwithme.resevation.service.response;

import team4.footwithme.resevation.domain.Mercenary;

public record MercenaryResponse(
        Long mercenaryId,
        Long reservationId,
        String description) {
    public MercenaryResponse(Mercenary mercenary) {
        this(
                mercenary.getMercenaryId(),
                mercenary.getReservation().getReservationId(),
                mercenary.getDescription()
        );
    }
}
