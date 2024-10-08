package team4.footwithme.resevation.service.response;

import team4.footwithme.resevation.domain.Mercenary;

public record MWMercenaryResponse(
        Long mercenaryId,
        Long reservationId,
        String description) {
    public MWMercenaryResponse(Mercenary mercenary) {
        this(
                mercenary.getMercenaryId(),
                mercenary.getReservation().getReservationId(),
                mercenary.getDescription()
        );
    }
}
